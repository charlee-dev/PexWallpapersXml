package com.adwi.pexwallpapers.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM wallpaper_table WHERE isFavorite = 1")
    fun getAllFavorites(): Flow<List<Wallpaper>>

    @Query("DELETE FROM wallpaper_table WHERE updatedAt < :timestampInMillis AND isFavorite = 0")
    suspend fun deleteNonFavoriteWallpapersOlderThan(timestampInMillis: Long)

    @Query("UPDATE wallpaper_table SET isFavorite = 0")
    suspend fun resetAllFavorites()

    @Update
    suspend fun updateWallpaper(wallpaper: Wallpaper)
}