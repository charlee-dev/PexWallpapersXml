package com.adwi.pexwallpapers.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.adwi.pexwallpapers.data.local.entity.Suggestion
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.local.entity.suggestionNameList
import com.adwi.pexwallpapers.data.repository.interfaces.SearchRepositoryInterface
import com.adwi.pexwallpapers.data.repository.interfaces.SettingsRepositoryInterface
import com.adwi.pexwallpapers.data.repository.interfaces.SuggestionsRepositoryInterface
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import com.adwi.pexwallpapers.di.IoDispatcher
import com.adwi.pexwallpapers.ui.base.BaseViewModel
import com.adwi.pexwallpapers.util.TypeConverter
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
    private val suggestionsRepository: SuggestionsRepositoryInterface,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val currentQuery = MutableStateFlow<String?>(null)

    val suggestions = suggestionsRepository.getAllSuggestions()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val hasCurrentQuery = currentQuery.map { it != null }

    val searchResults = currentQuery.flatMapLatest { query ->
        query?.let {
            searchRepository.getSearchResultsPaged(query)
        } ?: emptyFlow()
    }.cachedIn(viewModelScope)

    val wallpaperList = currentQuery.flatMapLatest { query ->
        query.let {
            wallpaperRepository.getWallpapersOfCategory(query!!)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    var refreshInProgress = false
    var pendingScrollToTopAfterRefresh = false
    var restoringSavedQuery = false
    var newQueryInProgress = false
    var pendingScrollToTopAfterNewQuery = false

    init {
        initDefaultSuggestionList()
        restoreLastQuery()
    }

    fun onSearchQuerySubmit(query: String) {
        currentQuery.value = query
        newQueryInProgress = true
        pendingScrollToTopAfterNewQuery = true
        restoringSavedQuery = false
        updateSavedQuery(query)
    }

    private fun updateSavedQuery(query: String) {
        onDispatcher(ioDispatcher) { settingsRepository.updateLastQuery(query) }
    }

    private fun restoreLastQuery() {
        onDispatcher(ioDispatcher) {
            val lastQuery = settingsRepository.getSettings().lastQuery
            currentQuery.value = lastQuery
            newQueryInProgress = false
            pendingScrollToTopAfterNewQuery = false
            restoringSavedQuery = true
        }
    }

    fun deleteSuggestion(name: String) {
        onDispatcher(ioDispatcher) { suggestionsRepository.deleteSuggestion(name) }
    }

    suspend fun addSuggestion(suggestion: Suggestion) {
        suggestionsRepository.insertSuggestion(suggestion)
    }

    private fun initDefaultSuggestionList() {
        if (suggestions.value.isNullOrEmpty()) {
            onDispatcher(ioDispatcher) {
                suggestionsRepository.insertAllSuggestions(
                    TypeConverter.defaultSuggestionNameListToSuggestions(suggestionNameList)
                )
            }
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