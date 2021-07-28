package com.adwi.pexwallpapers.data.local

import androidx.room.*
import com.adwi.pexwallpapers.data.local.entity.CuratedWallpapers
import com.adwi.pexwallpapers.data.local.entity.SearchResult
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpapersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpapers(wallpapers: List<Wallpaper>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCuratedWallpapers(wallpapers: List<CuratedWallpapers>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResults(searchResults: List<SearchResult>)

    @Update
    suspend fun updateWallpaperFavorite(wallpaper: Wallpaper)

    @Query("SELECT * FROM curated_wallpapers INNER JOIN wallpaper_table ON id = wallpaperId")
    fun getAllCuratedWallpapers(): Flow<List<Wallpaper>>

    @Query("SELECT MAX(queryPosition) FROM search_results WHERE searchQuery = :searchQuery")
    suspend fun getLastQueryPosition(searchQuery: String): Int?

    @Query("SELECT * FROM wallpaper_table")
    fun getAllWallpapers(): Flow<List<Wallpaper>>

    @Query("SELECT * FROM wallpaper_table WHERE isFavorite = 1")
    fun getAllFavorites(): Flow<List<Wallpaper>>

    @Query("UPDATE wallpaper_table SET isFavorite = 0")
    suspend fun resetAllFavorites()

    @Query("DELETE FROM search_results WHERE searchQuery = :query")
    suspend fun deleteSearchResultsForQuery(query: String)

    @Query("DELETE FROM curated_wallpapers")
    suspend fun deleteAllCuratedWallpapers()

    @Query("DELETE FROM wallpaper_table WHERE updatedAt < :timestampInMillis AND isFavorite = 0")
    suspend fun deleteNonFavoriteWallpapersOlderThan(timestampInMillis: Long)
}