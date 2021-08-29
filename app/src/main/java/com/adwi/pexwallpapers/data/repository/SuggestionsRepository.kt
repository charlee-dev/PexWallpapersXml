package com.adwi.pexwallpapers.data.repository

import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.Suggestion
import com.adwi.pexwallpapers.data.repository.interfaces.SuggestionsRepositoryInterface
import javax.inject.Inject

class SuggestionsRepository @Inject constructor(
    private val wallpapersDatabase: WallpaperDatabase
) : SuggestionsRepositoryInterface {

    private val dao = wallpapersDatabase.suggestionsDao()

    override fun getAllSuggestions() = dao.getAllSuggestions()

    override suspend fun insertSuggestion(suggestion: Suggestion) {
        dao.insertSuggestion(suggestion)
    }

    override suspend fun insertAllSuggestions(suggestions: List<Suggestion>) {
        dao.insertAllSuggestions(suggestions)
    }

    override suspend fun deleteSuggestion(name: String) = dao.deleteSuggestion(name)
}