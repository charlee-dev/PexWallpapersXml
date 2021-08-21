package com.adwi.pexwallpapers.ui.setwallpaper.infobottomsheet

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.adwi.pexwallpapers.databinding.FragmentInfoBottomSheetBinding
import com.adwi.pexwallpapers.shared.base.BaseBottomSheet
import com.adwi.pexwallpapers.ui.preview.bottomsheet.BottomSheetViewModel


class InfoBottomSheetFragment : BaseBottomSheet<FragmentInfoBottomSheetBinding, Any>(
    inflate = FragmentInfoBottomSheetBinding::inflate
) {
    override val viewModel: BottomSheetViewModel by viewModels()
    override val args: InfoBottomSheetFragmentArgs by navArgs()

    override fun setupViews() {
        binding.apply {
            wallpaper = args.wallpaper
        }
    }

    override fun setupAdapters() {}
    override fun setupFlows() {}
    override fun setupListeners() {}
}