package com.adwi.pexwallpapers.repository

import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.FavoritesRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class FakeFavoritesRepository : FavoritesRepositoryInterface {

    private val wallpapers = mutableListOf<Wallpaper>()
    private val wallpapersFlow = MutableStateFlow<List<Wallpaper>>(wallpapers)

    override fun getAllFavorites(): Flow<List<Wallpaper>> {
        return wallpapersFlow
    }

    override suspend fun deleteNonFavoriteWallpapersOlderThan(timestampInMillis: Long) {
        wallpapersFlow.collect { wallpapers ->
            wallpapers.forEach { wallpaper ->
                if (!wallpaper.isFavorite && wallpaper.updatedAt < timestampInMillis) {
                    deleteWallpaper(wallpaper)
                }
            }
        }
    }

    override suspend fun resetAllFavorites() {
        wallpapersFlow.collect { wallpapers ->
            wallpapers.forEach { wallpaper ->
                wallpaper.isFavorite = false
            }
        }
    }

    override suspend fun updateWallpaper(wallpaper: Wallpaper) {
        wallpapers.remove(wallpaper)
        wallpapers.add(wallpaper)
    }

    suspend fun deleteWallpaper(wallpaper: Wallpaper) {
        wallpapers.remove(wallpaper)
        refreshData()
    }

    private fun refreshData() {
        wallpapersFlow.value = wallpapers
    }
}