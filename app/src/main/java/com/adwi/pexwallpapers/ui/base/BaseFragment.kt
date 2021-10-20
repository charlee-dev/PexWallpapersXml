package com.adwi.pexwallpapers.ui.base

import android.Manifest
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.util.Constants.Companion.STORAGE_REQUEST_CODE
import com.adwi.pexwallpapers.util.launchCoroutine
import com.adwi.pexwallpapers.util.showIcons
import com.adwi.pexwallpapers.util.showSnackbar
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.coroutinespermission.PermissionManager
import timber.log.Timber

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T


abstract class BaseFragment<out VB : ViewDataBinding, AD : Any?>(
    private val inflate: Inflate<VB>
) : Fragment(), PopupMenu.OnMenuItemClickListener {

    protected abstract val viewModel: BaseViewModel?

    private var _binding: VB? = null
    val binding get() = _binding!!

    var mAdapter: AD? = null

    val TAG = this::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag(TAG).d(resources.getString(R.string.init_class))
        setupToolbar()
        setupAdapters()
        setupListeners()
        setupViews()
        setupFlows()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter = null
    }

    abstract fun setupToolbar()
    abstract fun setupAdapters()
    abstract fun setupViews()
    abstract fun setupFlows()
    abstract fun setupListeners()

    suspend fun checkStoragePermission(
        fragment: Fragment,
        granted: () -> Unit = {},
        denied: () -> Unit = {},
        deniedPermanently: () -> Unit = {}
    ) {
        Timber.tag(TAG).d("checkStoragePermission")
        //Resume coroutine once result is ready
        when (requestStoragePermission(fragment)) {
            is PermissionResult.PermissionGranted -> {
                Timber.tag(TAG).d("checkStoragePermission - granted")
                granted()
            }
            is PermissionResult.PermissionDenied -> {
                Timber.tag(TAG).d("checkStoragePermission - denied")
                showSnackbar(
                    message = requireContext().getString(R.string.auto_change_wallpaper_cannot_be_used_nwithout_storage_permission),
                    actionTitle = R.string.enable,
                    action = {
                        launchCoroutine {
                            requestStoragePermission(fragment)
                        }
                    }
                )
                denied()
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                viewModel?.setIsStoragePermissionGranted(false)
                Timber.tag(TAG).d("checkStoragePermission - deniedPermanently")
                deniedPermanently()
            }
            is PermissionResult.ShowRational -> {
                showRationaleDialog(
                    requireContext().getString(R.string.rationale_title),
                    requireContext().getString(R.string.rationale_desc)
                ) {
                    launchCoroutine {
                        val result = requestStoragePermission(fragment)
                        if (result is PermissionResult.PermissionGranted) granted()
                    }
                }
            }
        }
    }

    private suspend fun requestStoragePermission(fragment: Fragment): PermissionResult {
        Timber.tag(TAG).d("requestStoragePermission")
        return PermissionManager.requestPermissions(
            fragment,
            STORAGE_REQUEST_CODE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun showRationaleDialog(
        title: String,
        message: String,
        action: () -> Unit
    ) {
        Timber.tag(TAG).d("showRationaleDialog")
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ -> action() }
        builder.create().show()
    }

    protected fun navigateBack() {
        findNavController().popBackStack()
    }

    fun showMenu(v: View, menuId: Int) {
        PopupMenu(requireContext(), v).apply {
            menu.showIcons()
            setOnMenuItemClickListener(this@BaseFragment)
            inflate(menuId)
            show()
        }
    }

    fun showPopup(view: View, menuId: Int) {
        val popup = PopupMenu(requireContext(), view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(menuId, popup.menu)
        popup.show()
    }
}