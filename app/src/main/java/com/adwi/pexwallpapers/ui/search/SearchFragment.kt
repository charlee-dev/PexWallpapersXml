package com.adwi.pexwallpapers.ui.search

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Suggestion
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.FragmentSearchBinding
import com.adwi.pexwallpapers.shared.adapter.SuggestionListAdapter
import com.adwi.pexwallpapers.shared.adapter.WallpaperListPagingAdapter
import com.adwi.pexwallpapers.shared.adapter.WallpapersLoadStateAdapter
import com.adwi.pexwallpapers.ui.base.BaseFragment
import com.adwi.pexwallpapers.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class SearchFragment :
    BaseFragment<FragmentSearchBinding, WallpaperListPagingAdapter>(
        FragmentSearchBinding::inflate
    ) {
    override val viewModel: SearchViewModel by viewModels()

    private lateinit var wallpaperList: List<Wallpaper>
    private lateinit var suggestionList: List<Suggestion>

    private var _suggestionListAdapter: SuggestionListAdapter? = null
    private val suggestionListAdapter get() = _suggestionListAdapter

    private var filteredSuggestionList: ArrayList<Suggestion> = arrayListOf()

    override fun setupToolbar() {
        binding.apply {
            toolbarLayout.apply {
                searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
                    searchViewOnFocusBehaviour(hasFocus)
                }
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        newQuery(query!!)
                        addSuggestion(query)
                        return true
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        filteredSuggestionList = ArrayList()
                        if (query.isNullOrBlank()) {
                            if (this@SearchFragment::suggestionList.isInitialized) {
                                suggestionListAdapter?.submitList(suggestionList)
                            }
                        } else {
                            launchCoroutine {
                                query.let {
                                    suggestionList.forEach { suggestion ->
                                        if (suggestion.name.contains(query, true)) {
                                            filteredSuggestionList.add(suggestion)
                                            if (filteredSuggestionList.isNotEmpty()) {
                                                suggestionListAdapter?.submitList(
                                                    filteredSuggestionList.toList()
                                                )
                                            }
                                            suggestionsRecyclerView.scrollToPosition(0)
                                        }
                                    }
                                }
                            }
                        }
                        return true
                    }
                })
            }
        }
    }

    override fun setupAdapters() {
        mAdapter = WallpaperListPagingAdapter(
            onItemClick = { wallpaper ->
                var list = wallpaperList
                list = list.toMutableList()
                list.apply {
                    first().isFirst = true
                    last().isLast = true
                }
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToPreviewFragment(
                        wallpaper, list.toTypedArray()
                    )
                )
            },
            onItemLongClick = { wallpaper ->
                viewModel.onFavoriteClick(wallpaper)
            }
        )
        _suggestionListAdapter = SuggestionListAdapter(
            onItemClick = { suggestion ->
                newQuery(suggestion.name)
                searchViewOnFocusBehaviour(false)
            },
            onSuggestionDeleteClick = { suggestion ->
                launchCoroutine {
                    viewModel.deleteSuggestion(suggestion.name)
                    filteredSuggestionList.remove(suggestion)
                }
            }
        )
    }

    override fun setupViews() {
        binding.apply {
            shimmerFrameLayout.visibility = View.GONE
            recyclerView.apply {
                adapter = mAdapter?.withLoadStateFooter(
                    WallpapersLoadStateAdapter(mAdapter!!::retry)
                )
                layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                setHasFixedSize(true)
                itemAnimator?.changeDuration = 0
            }
            suggestionsRecyclerView.apply {
                adapter = suggestionListAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                itemAnimator = null
                itemAnimator?.changeDuration = 0
            }
        }
    }

    override fun setupListeners() {
        binding.apply {
            retryButton.setOnClickListener {
                mAdapter?.retry()
            }
            toolbarLayout.apply {
                menuButton.setOnClickListener {
                    showMenu(it, R.menu.menu_search_wallpaper)
                }
                backButton.setOnClickListener {
                    searchView.setQuery("", false)
                    searchViewOnFocusBehaviour(false)
                }
            }
        }
    }

    override fun setupFlows() {
        binding.apply {
            launchCoroutine {
                viewModel.suggestions.collect {
                    val suggestions = it ?: return@collect
                    suggestionList = suggestions
                    suggestionListAdapter?.submitList(suggestions)
                }
            }
            launchCoroutine {
                // collectLatest - as soon new data received, current block will be suspended
                viewModel.searchResults.collectLatest { data ->
                    mAdapter?.submitData(data)
                }
            }
            launchCoroutine {
                viewModel.currentQuery.collect { currentQuery ->
                    headerTextview.text = currentQuery
                }
            }
            launchCoroutine {
                viewModel.hasCurrentQuery.collect { hasCurrentQuery ->
                    instructionsTextview.isVisible = !hasCurrentQuery
                    recyclerView.isVisible = hasCurrentQuery
                }
            }
            launchCoroutine {
                mAdapter?.loadStateFlow
                    ?.distinctUntilChangedBy { it.source.refresh }
                    ?.filter { it.source.refresh is LoadState.NotLoading }
                    ?.collect {
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
            launchCoroutine {
                mAdapter?.loadStateFlow
                    ?.collect { loadState ->
                        when (val refresh = loadState.mediator?.refresh) {
                            is LoadState.Loading -> {
                                shimmerFrameLayout.visibility = View.VISIBLE
                                shimmerFrameLayout.startShimmer()
                                errorTextview.isVisible = false
                                retryButton.isVisible = false
                                noResultsTextview.isVisible = false
                                recyclerView.isVisible = mAdapter!!.itemCount > 0

                                // there was a bug, that recyclerView was showing old data for a split second
                                // this is work round it
                                recyclerView.showIfOrVisible {
                                    !viewModel.newQueryInProgress && mAdapter!!.itemCount > 0
                                }
                                shimmerFrameLayout.showIfOrVisible {
                                    viewModel.newQueryInProgress
                                }

                                viewModel.refreshInProgress = true
                                viewModel.pendingScrollToTopAfterRefresh = true
                            }
                            is LoadState.NotLoading -> {
                                shimmerFrameLayout.stopShimmer()
                                shimmerFrameLayout.isVisible = false
                                errorTextview.isVisible = false
                                retryButton.isVisible = false
                                recyclerView.isVisible = mAdapter!!.itemCount > 0

                                val noResults =
                                    mAdapter!!.itemCount < 1 && loadState.append.endOfPaginationReached
                                            && loadState.source.append.endOfPaginationReached

                                noResultsTextview.isVisible = noResults

                                viewModel.refreshInProgress = false
                                viewModel.newQueryInProgress = false
                            }
                            is LoadState.Error -> {
                                shimmerFrameLayout.stopShimmer()
                                shimmerFrameLayout.isVisible = false
                                noResultsTextview.isVisible = false
                                recyclerView.isVisible = mAdapter!!.itemCount > 0

                                val noCachedResults =
                                    mAdapter!!.itemCount < 1 && loadState.source.append.endOfPaginationReached

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
            launchCoroutine {
                viewModel.wallpaperList.collect {
                    val list = it ?: return@collect
                    wallpaperList = list
                }
            }
        }
    }

    private fun addSuggestion(name: String) {
        launchCoroutine {
            viewModel.addSuggestion(TypeConverter.suggestionNameToSuggestion(name))
        }
    }

    private fun searchViewOnFocusBehaviour(hasFocus: Boolean) {
        binding.apply {
            suggestionsRecyclerView.isVisible = hasFocus
            toolbarLayout.backButton.isVisible = hasFocus
            if (!hasFocus) filteredSuggestionList.clear()
        }
    }

    private fun newQuery(query: String) {
        viewModel.onSearchQuerySubmit(query)
        searchViewOnFocusBehaviour(false)
        binding.apply {
            toolbarLayout.searchView.clearFocus()
            searchConstrainLayout.transitionToStart()
            recyclerView.scrollToPosition(0)
        }
        hideKeyboard()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_refresh -> {
                mAdapter?.refresh()
                true
            }
            else -> false
        }
    }

    override fun onPause() {
        super.onPause()
        binding.apply {
            shimmerFrameLayout.stopShimmer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _suggestionListAdapter = null
    }
}