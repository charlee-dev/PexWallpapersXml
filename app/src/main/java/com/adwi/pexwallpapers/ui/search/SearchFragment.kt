package com.adwi.pexwallpapers.ui.search

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.databinding.FragmentSearchBinding
import com.adwi.pexwallpapers.shared.WallpaperListPagingAdapterAdapter
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.util.onQueryTextSubmit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {

    override val viewModel: SearchViewModel by viewModels()
    override val binding: FragmentSearchBinding by viewBinding(CreateMethod.INFLATE)

    private lateinit var wallpaperListAdapter: WallpaperListPagingAdapterAdapter

    override fun setupViews() {
        setHasOptionsMenu(true)

        wallpaperListAdapter = WallpaperListPagingAdapterAdapter(
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
                    swipeRefreshLayout.isEnabled = true
                    wallpaperListAdapter.submitData(data)
                }
            }

            swipeRefreshLayout.isEnabled = false

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                wallpaperListAdapter.loadStateFlow
                    .collect { loadState ->
                        when (val refresh = loadState.mediator?.refresh) {
                            is LoadState.Loading -> {
                                errorTextview.isVisible = false
                                retryButton.isVisible = false
                                swipeRefreshLayout.isRefreshing = true
                                noResultsTextview.isVisible = false
                                recyclerView.isVisible = wallpaperListAdapter.itemCount > 0
                            }
                            is LoadState.NotLoading -> {
                                errorTextview.isVisible = false
                                retryButton.isVisible = false
                                swipeRefreshLayout.isRefreshing = false
                                recyclerView.isVisible = wallpaperListAdapter.itemCount > 0

                                val noResults =
                                    wallpaperListAdapter.itemCount < 1 && loadState.append.endOfPaginationReached
                                            && loadState.source.append.endOfPaginationReached

                                noResultsTextview.isVisible = noResults
                            }
                            is LoadState.Error -> {
                                swipeRefreshLayout.isRefreshing = false
                                noResultsTextview.isVisible = false
                                recyclerView.isVisible = wallpaperListAdapter.itemCount > 0

                                val noCachedResults =
                                    wallpaperListAdapter.itemCount < 1 && loadState.source.append.endOfPaginationReached

                                errorTextview.isVisible = noCachedResults
                                retryButton.isVisible = noCachedResults

                                val errorMessage = getString(
                                    R.string.could_not_load_search_results,
                                    refresh.error.localizedMessage
                                        ?: getString(
                                            R.string.unknown_error_occurred
                                        )
                                )
                                errorTextview.text = errorMessage
                            }
                        }
                    }
            }

            swipeRefreshLayout.setOnRefreshListener {
                wallpaperListAdapter.refresh()
            }

            retryButton.setOnClickListener {
                wallpaperListAdapter.retry()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_wallpaper, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.onQueryTextSubmit { query ->
            viewModel.onSearchQuerySubmit(query)
            searchView.clearFocus()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_refresh -> {
                wallpaperListAdapter.refresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}