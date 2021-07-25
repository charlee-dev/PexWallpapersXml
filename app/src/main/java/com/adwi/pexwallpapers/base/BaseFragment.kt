package com.adwi.pexwallpapers.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.adwi.pexwallpapers.R
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d


abstract class BaseFragment<out VB : ViewDataBinding> : Fragment() {

    abstract val viewModel: ViewModel
    abstract val binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
//        setupListeners()
//        setupObservers()
//        setupAdapters()
    }

    override fun onStart() {
        super.onStart()

        Timber.tag(TAG).d { resources.getString(R.string.init_class) }

//        viewModel.showErrorMessage.observe(this, {
//            val toast = Toast.makeText(activity, it, Toast.LENGTH_LONG)
//            toast.show()
//        })
//
//        viewModel.showToast.observe(this, {
//            val toast = Toast.makeText(activity, it, Toast.LENGTH_LONG)
//
//            toast.show()
//        })
//
//        viewModel.showSnackBar.observe(this, {
//            val snackBar = Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG)
//            snackBar.addCallback(object : Snackbar.Callback() {
//                override fun onShown(sb: Snackbar?) {
//                    EspressoIdlingResource.increment()
//                }
//
//                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
//                    EspressoIdlingResource.decrement()
//                }
//            })
//            snackBar.show()
//        })
//
//        viewModel.showSnackBarInt.observe(this, {
//            val snackBar = Snackbar.make(requireView(), getString(it), Snackbar.LENGTH_LONG)
//            snackBar.addCallback(object : Snackbar.Callback() {
//
//                override fun onShown(sb: Snackbar?) {
//                    EspressoIdlingResource.increment()
//                }
//
//                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
//                    EspressoIdlingResource.decrement()
//                }
//            })
//            snackBar.show()
//        })
    }

    abstract fun setupViews()
//
//    abstract fun setupListeners()
//
//    abstract fun setupObservers()
//
//    abstract fun setupAdapters()

    companion object {
        private val TAG = this::class.java.simpleName
    }
}
