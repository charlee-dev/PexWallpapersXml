package com.adwi.pexwallpapers.shared.base

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


abstract class BaseFragment<out VB : ViewDataBinding, VM : ViewModel> : Fragment() {

    abstract val viewModel: VM
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
    }

    override fun onStart() {
        super.onStart()
        Timber.tag(TAG).d { resources.getString(R.string.init_class) }
    }

    abstract fun setupViews()

    companion object {
        private val TAG = this::class.java.simpleName
    }
}
