package com.adwi.pexwallpapers.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.adwi.pexwallpapers.data.WallpaperRepository
import com.adwi.pexwallpapers.data.local.entity.Suggestion
import com.adwi.pexwallpapers.data.local.entity.suggestionNameList
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import com.adwi.pexwallpapers.util.TypeConverter
import com.adwi.pexwallpapers.util.onIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: WallpaperRepository
) : BaseViewModel() {

    private var savedQuery: String? = null

    private val currentQuery = MutableStateFlow<String?>(null)

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
        savedQuery = savedStateHandle.get(SAVED_QUERY)
        if (savedQuery.isNullOrBlank()) {
            currentQuery.value = savedQuery
        }
    }

    fun onSearchQuerySubmit(query: String) {
        currentQuery.value = query
        newQueryInProgress = true
        pendingScrollToTopAfterNewQuery = true
        savedStateHandle.set(SAVED_QUERY, query)
    }

    fun deleteSuggestion(name: String) {
        onIO {
            repository.deleteSuggestion(name)
        }
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

    companion object {
        private const val SAVED_QUERY = "savedQuery"
    }
}