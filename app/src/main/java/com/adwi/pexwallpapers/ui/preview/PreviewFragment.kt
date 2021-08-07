package com.adwi.pexwallpapers.ui.preview

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.util.Constants.Companion.WALLPAPER_ID
import com.adwi.pexwallpapers.util.byPhotographer
import com.adwi.pexwallpapers.util.loadImageFromUrl
import com.adwi.pexwallpapers.util.slideUp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlin.properties.Delegates


@AndroidEntryPoint
class PreviewFragment :
    BaseFragment<FragmentPreviewBinding>(FragmentPreviewBinding::inflate, true) {

    override val viewModel: PreviewViewModel by viewModels()
    private var wallpaperId by Delegates.notNull<Int>()

    override fun setupViews() {
        val bundle = this.arguments
        if (bundle != null) {
            wallpaperId = bundle.getInt(WALLPAPER_ID)
        }

        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.getWallpaperById(wallpaperId).collect {
                    val wallpaper = it ?: return@collect
                    wallpaperImageView.loadImageFromUrl(wallpaper.imageUrl)
                    wallpaperPhotographerTextView.byPhotographer(wallpaper.photographer)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNav.slideUp()
        val fragmentManager = parentFragmentManager.beginTransaction()
        fragmentManager.remove(PreviewFragment())
            .commit()
    }
}