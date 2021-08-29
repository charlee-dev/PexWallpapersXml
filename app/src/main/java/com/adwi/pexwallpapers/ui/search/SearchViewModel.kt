package com.adwi.pexwallpapers.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.adwi.pexwallpapers.data.local.entity.Suggestion
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.local.entity.suggestionNameList
import com.adwi.pexwallpapers.data.repository.SearchRepository
import com.adwi.pexwallpapers.data.repository.SettingsRepository
import com.adwi.pexwallpapers.data.repository.SuggestionsRepository
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import com.adwi.pexwallpapers.util.TypeConverter
import com.adwi.pexwallpapers.util.onIO
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val settingsRepository: SettingsRepository,
    private val suggestionsRepository: SuggestionsRepository
) : BaseViewModel() {

    private val currentQuery = MutableStateFlow<String?>(null)

    var restoringSavedQuery: Boolean = false

    val suggestions = suggestionsRepository.getAllSuggestions()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val hasCurrentQuery = currentQuery.map { it != null }

    val searchResults = currentQuery.flatMapLatest { query ->
        query?.let {
            searchRepository.getSearchResultsPaged(query)
        } ?: emptyFlow()
    }.cachedIn(viewModelScope)

    var refreshInProgress = false
    var pendingScrollToTopAfterRefresh = false

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
        restoringSavedQuery = true
        updateSavedQuery(query)
    }

    private fun updateSavedQuery(query: String) {
        onIO { settingsRepository.updateLastQuery(query) }
    }

    private fun restoreLastQuery() {
        onIO {
            val lastQuery = settingsRepository.getSettings().lastQuery
            Timber.tag(TAG).d { "restored: $lastQuery" }
            currentQuery.value = lastQuery
            newQueryInProgress = false
            pendingScrollToTopAfterNewQuery = false
            restoringSavedQuery = true
        }
    }

    fun deleteSuggestion(name: String) {
        onIO { suggestionsRepository.deleteSuggestion(name) }
    }

    suspend fun addSuggestion(suggestion: Suggestion) {
        suggestionsRepository.insertSuggestion(suggestion)
    }

    private fun initDefaultSuggestionList() {
        if (suggestions.value.isNullOrEmpty()) {
            onIO {
                suggestionsRepository.insertAllSuggestions(
                    TypeConverter.defaultSuggestionNameListToSuggestions(suggestionNameList)
                )
            }
        }
    }

    fun onFavoriteClick(wallpaper: Wallpaper) {
        val isFavorite = wallpaper.isFavorite
        wallpaper.isFavorite = !isFavorite
        onIO {
            searchRepository.updateWallpaper(wallpaper)
        }
    }
}