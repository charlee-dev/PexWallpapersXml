package com.adwi.pexwallpapers.data.repository.interfaces

import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import kotlinx.coroutines.flow.Flow

interface FavoritesRepositoryInterface {

    fun getAllFavorites(): Flow<List<Wallpaper>>
    suspend fun deleteNonFavoriteWallpapersOlderThan(timestampInMillis: Long)
    suspend fun resetAllFavorites()
    suspend fun updateFavorites(wallpaper: Wallpaper)
}