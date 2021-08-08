package com.adwi.pexwallpapers.ui.preview

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.tools.WallpaperSetter
import com.adwi.pexwallpapers.util.Constants.Companion.WALLPAPER_ID
import com.adwi.pexwallpapers.util.byPhotographer
import com.adwi.pexwallpapers.util.byPhotographerContentDescription
import com.adwi.pexwallpapers.util.loadImageFromUrl
import com.adwi.pexwallpapers.util.slideUp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PreviewFragment :
    BaseFragment<FragmentPreviewBinding>(FragmentPreviewBinding::inflate, true) {

    override val viewModel: PreviewViewModel by viewModels()

    override fun setupViews() {

        val wallpaperId = arguments?.getInt(WALLPAPER_ID)

        if (wallpaperId != null) {
            binding.apply {
                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    viewModel.getWallpaperById(wallpaperId).collect {
                        val wallpaperFlow = it ?: return@collect

                        wallpaperImageView.loadImageFromUrl(wallpaperFlow.imageUrl)
                        wallpaperImageView.byPhotographerContentDescription(wallpaperFlow.photographer)
                        wallpaperPhotographerTextView.byPhotographer(wallpaperFlow.photographer)

                        setWallpaperButton.setOnClickListener {
                            viewLifecycleOwner.lifecycleScope.launch {
                                wallpaperFlow.imageUrl.let { url ->
                                    WallpaperSetter(requireContext()).setWallpaperByImagePath(
                                        requireActivity(),
                                        url
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNav.slideUp()
    }
}