package com.adwi.pexwallpapers.ui.search

import androidx.fragment.app.viewModels
import com.adwi.pexwallpapers.base.BaseFragment
import com.adwi.pexwallpapers.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    override val viewModel by viewModels<SearchViewModel>()

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