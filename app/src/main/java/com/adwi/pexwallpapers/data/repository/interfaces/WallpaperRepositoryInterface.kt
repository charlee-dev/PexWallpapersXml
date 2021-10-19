package com.adwi.pexwallpapers.data.repository.interfaces

import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.util.Resource
import kotlinx.coroutines.flow.Flow

interface WallpaperRepositoryInterface {

    fun getWallpapersOfCategory(categoryName: String): Flow<List<Wallpaper>>
    suspend fun getWallpaperById(wallpaperId: Int): Wallpaper
    suspend fun updateWallpaper(wallpaper: Wallpaper)

    fun getCuratedWallpapers(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchRemoteFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Wallpaper>>>
}