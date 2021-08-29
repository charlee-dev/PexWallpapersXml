package com.adwi.pexwallpapers.repository

import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import com.adwi.pexwallpapers.util.Resource
import com.adwi.pexwallpapers.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class FakeWallpaperRepository : WallpaperRepositoryInterface {

    private val wallpapers = mutableListOf<Wallpaper>()
    private val wallpapersFlow = MutableStateFlow<List<Wallpaper>>(wallpapers)

    override fun getWallpapersByCategory(categoryName: String): Flow<List<Wallpaper>> {
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

    suspend fun deleteAllWallpaper() {
        wallpapers.clear()
    }

    override suspend fun updateWallpaper(wallpaper: Wallpaper) {
        wallpapers.remove(wallpaper)
        wallpapers.add(wallpaper)
    }

    override fun getCuratedWallpapers(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchRemoteFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Wallpaper>>> = networkBoundResource(
        query = { wallpapersFlow },
        fetch = {},
        saveFetchResult = {

        },
        shouldFetch = { true },
        onFetchSuccess = {},
        onFetchFailed = {}
    )


    private fun refreshData() {
        wallpapersFlow.value = wallpapers
    }
}