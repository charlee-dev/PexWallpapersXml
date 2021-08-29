package com.adwi.pexwallpapers.data.repository.interfaces

import androidx.paging.PagingData
import com.adwi.pexwallpapers.data.local.dao.SearchDao
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import kotlinx.coroutines.flow.Flow

interface SearchRepositoryInterface {

    val dao: SearchDao

    fun getSearchResultsPaged(query: String): Flow<PagingData<Wallpaper>>

    suspend fun updateWallpaper(wallpaper: Wallpaper)
}