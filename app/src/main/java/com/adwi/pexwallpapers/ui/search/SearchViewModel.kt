package com.adwi.pexwallpapers.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.adwi.pexwallpapers.data.WallpaperRepository
import com.adwi.pexwallpapers.data.local.entity.Suggestion
import com.adwi.pexwallpapers.data.local.entity.suggestionNameList
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
    private val repository: WallpaperRepository
) : BaseViewModel() {

    private val currentQuery = MutableStateFlow<String?>(null)

    var restoringSavedQuery: Boolean = false

    val suggestions = repository.getAllSuggestions()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val hasCurrentQuery = currentQuery.map { it != null }

    val searchResults = currentQuery.flatMapLatest { query ->
        query?.let {
            repository.getSearchResultsPaged(query)
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
        onIO { repository.updateLastQuery(query) }
    }

    private fun restoreLastQuery() {
        onIO {
            val lastQuery = repository.getSettings().lastQuery
            Timber.tag(TAG).d { "restored: $lastQuery" }
            currentQuery.value = lastQuery
            newQueryInProgress = false
            pendingScrollToTopAfterNewQuery = false
            restoringSavedQuery = true
        }
    }

    fun deleteSuggestion(name: String) {
        onIO { repository.deleteSuggestion(name) }
    }

    suspend fun addSuggestion(suggestion: Suggestion) {
        repository.insertSuggestion(suggestion)
    }

    private fun initDefaultSuggestionList() {
        if (suggestions.value.isNullOrEmpty()) {
            onIO {
                repository.insertAllSuggestions(
                    TypeConverter.defaultSuggestionNameListToSuggestions(suggestionNameList)
                )
            }
        }
    }
}