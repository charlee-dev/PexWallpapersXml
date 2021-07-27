package com.adwi.pexwallpapers.data.local

import androidx.room.*
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpapersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpapers(wallpapers: List<Wallpaper>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaper(wallpaper: Wallpaper)

    @Query("SELECT * FROM wallpaper_table")
    fun getAllWallpapers(): Flow<List<Wallpaper>>

    @Query("SELECT * FROM wallpaper_table WHERE isFavorite = 1")
    fun getAllFavorites(): Flow<List<Wallpaper>>

    @Update
    suspend fun updateWallpaperFavorite(wallpaper: Wallpaper)

    @Query("UPDATE wallpaper_table SET isFavorite = 0")
    suspend fun resetAllFavorites()

    @Query("DELETE FROM wallpaper_table")
    suspend fun deleteAllWallpapers()

    @Query("DELETE FROM wallpaper_table WHERE updatedAt < :timestampInMillis AND isFavorite = 0")
    suspend fun deleteNonFavoriteWallpapersOlderThan(timestampInMillis: Long)
}