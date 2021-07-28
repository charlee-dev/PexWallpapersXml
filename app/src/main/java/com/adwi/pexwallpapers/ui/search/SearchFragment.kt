package com.adwi.pexwallpapers.ui.search

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adwi.pexwallpapers.databinding.FragmentSearchBinding
import com.adwi.pexwallpapers.shared.WallpaperListPagingAdapterAdapter
import com.adwi.pexwallpapers.shared.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {

    override val viewModel: SearchViewModel by viewModels()
    override val binding: FragmentSearchBinding by viewBinding(CreateMethod.INFLATE)

    override fun setupViews() {
        val wallpaperListAdapter = WallpaperListPagingAdapterAdapter(
            onItemClick = { wallpaper ->
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToPreviewFragment(
                        wallpaper.id
                    )
                )
            },
            onFavoriteClick = { wallpaper ->
                viewModel.onFavoriteClick(wallpaper)
            }
        )

        binding.apply {
            recyclerView.apply {
                adapter = wallpaperListAdapter.withLoadStateFooter(
                    WallpapersLoadStateAdapter(wallpaperListAdapter::retry)
                )
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                itemAnimator?.changeDuration = 0
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                // collectLatest - as soon new data received, current block will be suspended
                viewModel.searchResults.collectLatest { data ->
                    instructionsTextview.isVisible = false
                    wallpaperListAdapter.submitData(data)

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onSearchQuerySubmit("poland")
    }
}