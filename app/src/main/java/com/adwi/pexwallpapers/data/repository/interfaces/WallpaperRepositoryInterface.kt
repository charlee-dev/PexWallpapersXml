package com.adwi.pexwallpapers.data.repository.interfaces

import com.adwi.pexwallpapers.data.local.dao.WallpapersDao
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.util.Resource
import kotlinx.coroutines.flow.Flow

interface WallpaperRepositoryInterface {

    val wallpaperDao: WallpapersDao

    fun getWallpaper(wallpaperId: Int): Flow<Wallpaper>
    fun getWallpapersByCategory(categoryName: String): Flow<List<Wallpaper>>
    suspend fun updateWallpaper(wallpaper: Wallpaper)

    fun getCuratedWallpapers(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchRemoteFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Wallpaper>>>
}