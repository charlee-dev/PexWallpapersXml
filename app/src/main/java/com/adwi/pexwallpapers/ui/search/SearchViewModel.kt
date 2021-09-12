package com.adwi.pexwallpapers.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.SearchRepositoryInterface
import com.adwi.pexwallpapers.data.repository.interfaces.SettingsRepositoryInterface
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import com.adwi.pexwallpapers.di.IoDispatcher
import com.adwi.pexwallpapers.ui.base.BaseViewModel
import com.adwi.pexwallpapers.util.onDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepositoryInterface,
    private val searchRepository: SearchRepositoryInterface,
    private val settingsRepository: SettingsRepositoryInterface,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    val currentQuery = MutableStateFlow<String?>(null)

    val hasCurrentQuery = currentQuery.map { it != null }

    val searchResults = currentQuery.flatMapLatest { query ->
        query?.let {
            searchRepository.getSearchResultsPaged(query)
        } ?: emptyFlow()
    }.cachedIn(viewModelScope)

    val wallpaperList = currentQuery.flatMapLatest { query ->
        query?.let {
            wallpaperRepository.getWallpapersOfCategory(query)
        } ?: emptyFlow()
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var refreshInProgress = false
    var pendingScrollToTopAfterRefresh = false
    var newQueryInProgress = false
    var pendingScrollToTopAfterNewQuery = false

    init {
        restoreLastQuery()
    }

    fun onSearchQuerySubmit(query: String) {
        currentQuery.value = query
        newQueryInProgress = true
        pendingScrollToTopAfterNewQuery = true
        updateSavedQuery(query)
    }

    private fun updateSavedQuery(query: String) {
        onDispatcher(ioDispatcher) { settingsRepository.updateLastQuery(query) }
    }

    private fun restoreLastQuery() {
        onDispatcher(ioDispatcher) {
            currentQuery.value = settingsRepository.getSettings().lastQuery
            newQueryInProgress = false
            pendingScrollToTopAfterNewQuery = false
        }
    }

    fun onFavoriteClick(wallpaper: Wallpaper) {
        val isFavorite = wallpaper.isFavorite
        wallpaper.isFavorite = !isFavorite
        onDispatcher(ioDispatcher) {
            searchRepository.updateWallpaper(wallpaper)
        }
    }
}