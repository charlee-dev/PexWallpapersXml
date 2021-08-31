package com.adwi.pexwallpapers.repository

import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import com.adwi.pexwallpapers.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

open class FakeWallpaperRepository : WallpaperRepositoryInterface {

    private val wallpapers = mutableListOf<Wallpaper>()
    private val wallpapersFlow = MutableStateFlow<List<Wallpaper>>(wallpapers)

    override fun getWallpapersOfCategory(categoryName: String): Flow<List<Wallpaper>> {
        return wallpapersFlow
    }

    fun getWallpapers(): Flow<List<Wallpaper>> = flow { emit(wallpapers.toList()) }

    suspend fun insert(wallpaper: Wallpaper) {
        wallpapers.add(wallpaper)
        refreshData()
    }

    suspend fun deleteWallpaper(wallpaper: Wallpaper) {
        wallpapers.remove(wallpaper)
        refreshData()
    }

    suspend fun deleteAllWallpapers() {
        wallpapers.clear()
        refreshData()
    }

    override suspend fun updateWallpaper(wallpaper: Wallpaper) {
        wallpapers.remove(wallpaper)
        wallpapers.add(wallpaper)
        refreshData()
    }

    override fun getCuratedWallpapers(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchRemoteFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Wallpaper>>> = flow { emit(Resource.Success(wallpapers.toList())) }

    private fun refreshData() {
        wallpapersFlow.value = wallpapers
    }
}