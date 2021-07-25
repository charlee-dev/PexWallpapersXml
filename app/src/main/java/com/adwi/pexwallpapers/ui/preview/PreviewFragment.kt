package com.adwi.pexwallpapers.ui.preview

import androidx.fragment.app.viewModels
import com.adwi.pexwallpapers.base.BaseFragment
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewFragment : BaseFragment<FragmentPreviewBinding>(FragmentPreviewBinding::inflate) {

    override val viewModel by viewModels<PreviewViewModel>()

    override fun setupViews() {
        TODO("Not yet implemented")
    }

    override fun setupListeners() {
        TODO("Not yet implemented")
    }

    override fun setupObservers() {
        TODO("Not yet implemented")
    }
}