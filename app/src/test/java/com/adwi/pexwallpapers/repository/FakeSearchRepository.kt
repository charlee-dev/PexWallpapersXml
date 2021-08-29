package com.adwi.pexwallpapers.repository

import androidx.paging.PagingData
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.SearchRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSearchRepository : SearchRepositoryInterface {

    private val wallpapers = mutableListOf<Wallpaper>()
    private val wallpapersFlow = MutableStateFlow<List<Wallpaper>>(wallpapers)

    override fun getSearchResultsPaged(query: String): Flow<PagingData<Wallpaper>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateWallpaper(wallpaper: Wallpaper) {
        wallpapers.remove(wallpaper)
        wallpapers.add(wallpaper)
    }
}