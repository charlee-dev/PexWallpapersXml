package com.adwi.pexwallpapers.ui.favorites

import androidx.fragment.app.viewModels
import com.adwi.pexwallpapers.base.BaseFragment
import com.adwi.pexwallpapers.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(
    FragmentFavoritesBinding::inflate
) {

    override val viewModel by viewModels<FavoritesViewModel>()

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