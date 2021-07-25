package com.adwi.pexwallpapers.ui.wallpapers

import androidx.fragment.app.viewModels
import com.adwi.pexwallpapers.base.BaseFragment
import com.adwi.pexwallpapers.databinding.FragmentWallpapersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WallpapersFragment : BaseFragment<FragmentWallpapersBinding>(
    FragmentWallpapersBinding::inflate
) {

    override val viewModel by viewModels<WallpaperViewModel>()

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