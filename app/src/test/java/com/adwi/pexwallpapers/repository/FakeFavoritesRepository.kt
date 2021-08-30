package com.adwi.pexwallpapers.repository

import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.FavoritesRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class FakeFavoritesRepository : FavoritesRepositoryInterface {

    private val wallpapers = mutableListOf<Wallpaper>()
    private val wallpapersFlow = MutableStateFlow<List<Wallpaper>>(wallpapers)

    fun insertWallpaper(wallpaper: Wallpaper) {
        wallpapers.add(wallpaper)
        refreshFlow()
    }

    override fun getAllFavorites() = wallpapersFlow

    override suspend fun deleteNonFavoriteWallpapersOlderThan(timestampInMillis: Long) {
        wallpapersFlow.collect { wallpapers ->
            wallpapers.forEach { wallpaper ->
                if (!wallpaper.isFavorite && wallpaper.updatedAt < timestampInMillis) {
                    wallpaper.isFavorite = false
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

    override suspend fun updateFavorites(wallpaper: Wallpaper) {
        wallpapers.remove(wallpaper)
        wallpapers.add(wallpaper)
        refreshFlow()
    }

    suspend fun deleteFavorites(wallpaper: Wallpaper) {
        wallpapers.remove(wallpaper)
        refreshFlow()
    }

    private fun refreshFlow() {
        wallpapersFlow.value = wallpapers
    }
}