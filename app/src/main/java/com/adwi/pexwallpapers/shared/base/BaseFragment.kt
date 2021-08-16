package com.adwi.pexwallpapers.shared.base

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.util.slideDown
import com.adwi.pexwallpapers.util.slideUp
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import com.google.android.material.bottomnavigation.BottomNavigationView

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<out VB : ViewDataBinding, AD : Any?>(
    private val inflate: Inflate<VB>,
    private val hasNavigation: Boolean
) : Fragment(), PopupMenu.OnMenuItemClickListener {

    protected abstract val viewModel: BaseViewModel?

    // Views
    lateinit var bottomNav: BottomNavigationView

    // Binding
    private var _binding: VB? = null
    val binding get() = _binding!!

    // Adapter
    var mAdapter: AD? = null

    val TAG = this::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        bottomNav = requireActivity().findViewById(R.id.bottom_nav)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag(TAG).d { resources.getString(R.string.init_class) }
        if (!hasNavigation) bottomNav.slideDown()
        setupToolbar()
        setupAdapters()
        setupViews()
        setupFlows()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter = null
        if (!hasNavigation) bottomNav.slideUp()
    }

    abstract fun setupToolbar()
    abstract fun setupAdapters()
    abstract fun setupViews()
    abstract fun setupFlows()
    abstract fun setupListeners()

    protected fun navigateBack() {
        Navigation.findNavController(requireView()).popBackStack()
    }

    fun showMenu(v: View, menuId: Int) {
        PopupMenu(requireContext(), v).apply {
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