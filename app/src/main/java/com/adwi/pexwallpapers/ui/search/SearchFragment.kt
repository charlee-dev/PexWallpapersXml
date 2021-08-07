package com.adwi.pexwallpapers.ui.search

import android.content.Intent
import android.net.Uri
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
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.databinding.FragmentSearchBinding
import com.adwi.pexwallpapers.shared.WallpaperListPagingAdapterAdapter
import com.adwi.pexwallpapers.shared.WallpapersLoadStateAdapter
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.tools.SharingTools
import com.adwi.pexwallpapers.util.onQueryTextSubmit
import com.adwi.pexwallpapers.util.showIfOrVisible
import com.adwi.pexwallpapers.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class SearchFragment :
    BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate, false) {

    override val viewModel: SearchViewModel by viewModels()

    private lateinit var wallpaperListAdapter: WallpaperListPagingAdapterAdapter

    override fun setupViews() {
        setHasOptionsMenu(true)

        wallpaperListAdapter = WallpaperListPagingAdapterAdapter(
            onItemClick = { wallpaper ->
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToPreviewFragment(
                        wallpaper
                    )
                )
            },
            onShareClick = { wallpaper ->
                wallpaper.url?.let {
                    SharingTools(requireContext())
                        .share(it)
                }
            },
            onFavoriteClick = { wallpaper ->
                viewModel.onFavoriteClick(wallpaper)
            },
            onPexelLogoClick = { wallpaper ->
                val uri = Uri.parse(wallpaper.url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                requireActivity().startActivity(intent)
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
                    wallpaperListAdapter.submitData(data)
                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.hasCurrentQuery.collect { hasCurrentQuery ->
                    instructionsTextview.isVisible = !hasCurrentQuery
                    swipeRefreshLayout.isEnabled = hasCurrentQuery
                    if (!hasCurrentQuery) {
                        recyclerView.isVisible = false
                    }

                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                wallpaperListAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.source.refresh }
                    .filter { it.source.refresh is LoadState.NotLoading }
                    .collect {
                        if (viewModel.pendingScrollToTopAfterRefresh && it.mediator?.refresh is LoadState.NotLoading) {
                            recyclerView.smoothScrollToPosition(0)
                            viewModel.pendingScrollToTopAfterRefresh = false
                            if (viewModel.pendingScrollToTopAfterNewQuery) {
                                recyclerView.smoothScrollToPosition(0)
                                viewModel.pendingScrollToTopAfterNewQuery = false
                            }
                            if (viewModel.pendingScrollToTopAfterNewQuery && it.mediator?.refresh is LoadState.NotLoading) {
                                recyclerView.smoothScrollToPosition(0)
                                viewModel.pendingScrollToTopAfterNewQuery = false
                            }
                        }
                    }
            }

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

                                // there was a bug, that recyclerView was showing old data for a split second
                                // this is work round it
                                recyclerView.showIfOrVisible {
                                    !viewModel.newQueryInProgress && wallpaperListAdapter.itemCount > 0
                                }

                                viewModel.refreshInProgress = true
                                viewModel.pendingScrollToTopAfterRefresh = true
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

                                viewModel.refreshInProgress = false
                                viewModel.newQueryInProgress = false
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

                                if (viewModel.refreshInProgress) {
                                    showSnackbar(errorMessage)
                                }
                                viewModel.refreshInProgress = false
                                viewModel.pendingScrollToTopAfterRefresh = false
                                viewModel.newQueryInProgress = false
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