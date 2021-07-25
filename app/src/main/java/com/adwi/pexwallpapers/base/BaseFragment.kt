package com.adwi.pexwallpapers.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.adwi.pexwallpapers.util.EspressoIdlingResource
import com.google.android.material.snackbar.Snackbar


abstract class BaseFragment : Fragment() {

    abstract val _viewModel: BaseViewModel

    override fun onStart() {
        super.onStart()

        _viewModel.showErrorMessage.observe(this, {
            val toast = Toast.makeText(activity, it, Toast.LENGTH_LONG)
            toast.show()
        })

        _viewModel.showToast.observe(this, {
            val toast = Toast.makeText(activity, it, Toast.LENGTH_LONG)

            toast.show()
        })

        _viewModel.showSnackBar.observe(this, {
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

        _viewModel.showSnackBarInt.observe(this, {
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
}