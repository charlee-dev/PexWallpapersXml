package com.adwi.pexwallpapers.shared.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.util.slideDown
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import com.google.android.material.bottomnavigation.BottomNavigationView

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<out VB : ViewDataBinding>(
    private val inflate: Inflate<VB>,
    private val hideNavigation: Boolean,

    ) : Fragment() {

    protected abstract val viewModel: BaseViewModel?

    private var _binding: VB? = null
    val binding get() = _binding!!

    lateinit var bottomNav: BottomNavigationView
    val TAG = this::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        bottomNav = requireActivity().findViewById(R.id.bottom_nav)
        navigationVisibility(hideNavigation)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun navigationVisibility(hideNavigation: Boolean) {
        bottomNav.apply {
            if (hideNavigation) slideDown() else visibility = View.VISIBLE
        }
    }
}
