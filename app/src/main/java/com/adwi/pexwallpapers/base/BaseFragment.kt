package com.adwi.pexwallpapers.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.util.EspressoIdlingResource
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import com.google.android.material.snackbar.Snackbar

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    protected abstract val viewModel: BaseViewModel
    private var _binding: VB? = null
    val binding get() = _binding!!

    companion object {
        private val TAG = this::class.java.simpleName
    }

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

        setupViews()
        setupListeners()
        setupObservers()
    }

    override fun onStart() {
        super.onStart()

        Timber.tag(TAG).d { resources.getString(R.string.init_class) }

        viewModel.showErrorMessage.observe(this, {
            val toast = Toast.makeText(activity, it, Toast.LENGTH_LONG)
            toast.show()
        })

        viewModel.showToast.observe(this, {
            val toast = Toast.makeText(activity, it, Toast.LENGTH_LONG)

            toast.show()
        })

        viewModel.showSnackBar.observe(this, {
            val snackBar = Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG)
            snackBar.addCallback(object : Snackbar.Callback() {
                override fun onShown(sb: Snackbar?) {
                    EspressoIdlingResource.increment()
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    EspressoIdlingResource.decrement()
                }
            })
            snackBar.show()
        })

        viewModel.showSnackBarInt.observe(this, {
            val snackBar = Snackbar.make(requireView(), getString(it), Snackbar.LENGTH_LONG)
            snackBar.addCallback(object : Snackbar.Callback() {

                override fun onShown(sb: Snackbar?) {
                    EspressoIdlingResource.increment()
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    EspressoIdlingResource.decrement()
                }
            })
            snackBar.show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun setupViews()

    abstract fun setupListeners()

    abstract fun setupObservers()
}