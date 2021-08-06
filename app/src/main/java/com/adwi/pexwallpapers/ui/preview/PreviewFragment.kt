package com.adwi.pexwallpapers.ui.preview

import androidx.fragment.app.viewModels
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.util.byPhotographer
import com.adwi.pexwallpapers.util.loadImageFromUrl
import com.adwi.pexwallpapers.util.slideDown
import com.adwi.pexwallpapers.util.slideUp
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PreviewFragment(private val wallpaper: Wallpaper) :
    BaseFragment<FragmentPreviewBinding, PreviewViewModel>(FragmentPreviewBinding::inflate) {

    override val viewModel: PreviewViewModel by viewModels()

    private lateinit var bottomNav: BottomNavigationView

    override fun setupViews() {

        bottomNav = requireActivity().findViewById(R.id.bottom_nav)

        bottomNav.slideDown()
        binding.apply {
            wallpaperImageView.loadImageFromUrl(wallpaper.imageUrl)
            wallpaperPhotographerTextView.byPhotographer(wallpaper.photographer)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNav.slideUp()
    }
}