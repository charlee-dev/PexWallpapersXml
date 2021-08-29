package com.adwi.pexwallpapers.data.repository.interfaces

import com.adwi.pexwallpapers.data.local.dao.SuggestionsDao
import com.adwi.pexwallpapers.data.local.entity.Suggestion
import kotlinx.coroutines.flow.Flow

interface SuggestionsRepositoryInterface {

    val dao: SuggestionsDao

    fun getAllSuggestions(): Flow<List<Suggestion>>
    suspend fun insertSuggestion(suggestion: Suggestion)
    suspend fun insertAllSuggestions(suggestions: List<Suggestion>)
    suspend fun deleteSuggestion(name: String)
}