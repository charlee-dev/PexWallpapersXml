package com.adwi.pexwallpapers.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.adwi.pexwallpapers.data.SearchNewsRemoteMediator
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.remote.PexApi
import com.adwi.pexwallpapers.data.repository.interfaces.SearchRepositoryInterface
import com.adwi.pexwallpapers.util.Constants
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val pexApi: PexApi,
    private val wallpapersDatabase: WallpaperDatabase,
) : SearchRepositoryInterface {

    private val dao = wallpapersDatabase.searchDao()

    override fun getSearchResultsPaged(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = Constants.PAGING_SIZE,
                maxSize = Constants.PAGING_MAX_SIZE
            ),
            remoteMediator = SearchNewsRemoteMediator(query, pexApi, wallpapersDatabase),
            pagingSourceFactory = { dao.getSearchResultWallpaperPaged(query) }
        ).flow

    override suspend fun updateWallpaper(wallpaper: Wallpaper) =
        dao.updateWallpaperFavorite(wallpaper)
}