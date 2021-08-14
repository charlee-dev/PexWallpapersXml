package com.adwi.pexwallpapers.shared.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.util.slideDown
import com.adwi.pexwallpapers.util.slideUp
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import com.google.android.material.bottomnavigation.BottomNavigationView

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<out VB : ViewDataBinding, AD : Any?>(
    private val inflate: Inflate<VB>,
    private val hasBackButton: Boolean,
    private val hasOptionsMenu: Boolean,
    private val hasNavigation: Boolean
) : Fragment() {

    protected abstract val viewModel: BaseViewModel?

    private var _binding: VB? = null
    val binding get() = _binding!!

    var mAdapter: AD? = null

    lateinit var bottomNav: BottomNavigationView

    val TAG = this::class.java.simpleName

    override fun onStart() {
        super.onStart()
        Timber.tag(TAG).d { resources.getString(R.string.init_class) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        bottomNav = requireActivity().findViewById(R.id.bottom_nav)
        setHasOptionsMenu(hasOptionsMenu)
        navigationVisibility(hasNavigation)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasNavigation) bottomNav.slideDown()
        setupAdapter()
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter = null
        if (!hasNavigation) bottomNav.slideUp()
    }

    abstract fun setupToolbar()
    abstract fun setupAdapter()
    abstract fun setupViews()

    private fun navigationVisibility(hideNavigation: Boolean) =
        bottomNav.apply {
            if (hideNavigation) visibility = View.VISIBLE else slideDown()
        }
}