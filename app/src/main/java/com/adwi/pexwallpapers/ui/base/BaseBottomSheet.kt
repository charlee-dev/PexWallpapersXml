package com.adwi.pexwallpapers.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavArgs
import com.adwi.pexwallpapers.R
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseBottomSheet<VB : ViewDataBinding, AD : Any?>(
    private val inflate: Inflate<VB>
) : BottomSheetDialogFragment() {

    protected abstract val viewModel: BaseViewModel?
    protected abstract val args: NavArgs

    var _binding: VB? = null
    val binding get() = _binding!!

    var _mAdapter: AD? = null

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
        Timber.tag(TAG).d { resources.getString(R.string.init_class) }
        setupAdapters()
        setupListeners()
        setupViews()
        setupFlows()
    }

    abstract fun setupAdapters()
    abstract fun setupViews()
    abstract fun setupFlows()
    abstract fun setupListeners()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _mAdapter = null
    }
}