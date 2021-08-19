package com.adwi.pexwallpapers.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adwi.pexwallpapers.data.local.entity.Suggestion
import kotlinx.coroutines.flow.Flow

@Dao
interface SuggestionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSuggestions(suggestions: List<Suggestion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestion: Suggestion)

    @Query("SELECT * FROM suggestions_table")
    fun getAllSuggestions(): Flow<List<Suggestion>>

    @Query("SELECT * FROM suggestions_table WHERE name = '%' || :query || '%'")
    fun getAllSuggestionsContainingQuery(query: String): Flow<List<Suggestion>>

    @Query("SELECT * FROM suggestions_table WHERE isAddedOnSubmit = 1")
    fun getAllSuggestionsAddedOnSubmit(): Flow<List<Suggestion>>

    @Query("DELETE FROM search_results WHERE searchQuery = :suggestion")
    suspend fun deleteSuggestion(suggestion: String)
}