package com.adwi.pexwallpapers.data.repository.interfaces

import com.adwi.pexwallpapers.data.local.entity.Suggestion
import kotlinx.coroutines.flow.Flow

interface SuggestionsRepositoryInterface {

    fun getAllSuggestions(): Flow<List<Suggestion>>
    suspend fun insertSuggestion(suggestion: Suggestion)
    suspend fun insertAllSuggestions(suggestions: List<Suggestion>)
    suspend fun deleteSuggestion(name: String)
}