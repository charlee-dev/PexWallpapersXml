package com.adwi.pexwallpapers.ui.preview

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.util.slideUp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PreviewFragment :
    BaseFragment<FragmentPreviewBinding>(FragmentPreviewBinding::inflate, true) {

    override val viewModel: PreviewViewModel by viewModels()
    private val args: PreviewFragmentArgs by navArgs()

    override fun setupViews() {


        binding.apply {
            wallpaper = args.wallpaper
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNav.slideUp()
    }
}