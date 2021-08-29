package com.adwi.pexwallpapers.repository

import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import com.adwi.pexwallpapers.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class FakeWallpaperRepository : WallpaperRepositoryInterface {

    private val wallpapers = mutableListOf<Wallpaper>()
    private val wallpapersFlow = MutableStateFlow<List<Wallpaper>>(wallpapers)

    override fun getWallpapersByCategory(categoryName: String): Flow<List<Wallpaper>> {
        return wallpapersFlow
    }

    suspend fun insertAll(wallpaper: Wallpaper) {
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
    ): Flow<Resource<List<Wallpaper>>> {
        var wallpapersResourceFlow: Flow<Resource<List<Wallpaper>>> = flow {
            emit(
                Resource.Success(
                    emptyList()
                )
            )
        }
        runBlocking {
            wallpapersFlow.collect {
                wallpapersResourceFlow = flow {
                    emit((Resource.Success(it)))
                }
            }
        }
        return wallpapersResourceFlow
    }

    private fun refreshData() {
        wallpapersFlow.value = wallpapers
    }
}