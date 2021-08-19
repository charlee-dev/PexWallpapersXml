package com.adwi.pexwallpapers.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adwi.pexwallpapers.data.local.entity.SearchResult
import com.adwi.pexwallpapers.data.local.entity.Wallpaper

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResults(searchResults: List<SearchResult>)

    @Query("SELECT * FROM search_results INNER JOIN wallpaper_table ON id = wallpaperId WHERE searchQuery = :query ORDER BY queryPosition")
    fun getSearchResultWallpaperPaged(query: String): PagingSource<Int, Wallpaper>

    @Query("SELECT MAX(queryPosition) FROM search_results WHERE searchQuery = :searchQuery")
    suspend fun getLastQueryPosition(searchQuery: String): Int?

    @Query("DELETE FROM search_results WHERE searchQuery = :query")
    suspend fun deleteSearchResultsForQuery(query: String)
}