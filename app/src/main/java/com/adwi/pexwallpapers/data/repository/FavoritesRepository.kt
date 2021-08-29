package com.adwi.pexwallpapers.data.repository

import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.FavoritesRepositoryInterface
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val wallpapersDatabase: WallpaperDatabase
) : FavoritesRepositoryInterface {

    private val dao = wallpapersDatabase.favoritesDao()

    override fun getAllFavorites() = dao.getAllFavorites()

    override suspend fun deleteNonFavoriteWallpapersOlderThan(timestampInMillis: Long) =
        dao.deleteNonFavoriteWallpapersOlderThan(timestampInMillis)

    override suspend fun resetAllFavorites() = dao.resetAllFavorites()

    override suspend fun updateWallpaper(wallpaper: Wallpaper) = dao.updateWallpaper(wallpaper)
}