package com.adwi.pexwallpapers.data.repository

import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.Suggestion
import com.adwi.pexwallpapers.data.repository.interfaces.SuggestionsRepositoryInterface
import com.adwi.pexwallpapers.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SuggestionsRepository @Inject constructor(
    private val wallpapersDatabase: WallpaperDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SuggestionsRepositoryInterface {

    private val dao = wallpapersDatabase.suggestionsDao()

    override fun getAllSuggestions() = dao.getAllSuggestions().flowOn(ioDispatcher)

    override suspend fun insertSuggestion(suggestion: Suggestion) {
        dao.insertSuggestion(suggestion)
    }

    override suspend fun insertAllSuggestions(suggestions: List<Suggestion>) {
        dao.insertAllSuggestions(suggestions)
    }

    override suspend fun deleteSuggestion(name: String) = dao.deleteSuggestion(name)
}