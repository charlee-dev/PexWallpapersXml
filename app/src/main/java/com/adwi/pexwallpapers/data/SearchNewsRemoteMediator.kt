package com.adwi.pexwallpapers.data

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.WallpapersDao
import com.adwi.pexwallpapers.data.local.entity.SearchQueryRemoteKey
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.remote.PexApi
import com.adwi.pexwallpapers.util.TypeConverter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

private const val WALLPAPERS_STARTING_PAGE_INDEX = 1

class SearchNewsRemoteMediator(
    private val searchQuery: String,
    private val pexApi: PexApi,
    private val database: WallpaperDatabase
) : RemoteMediator<Int, Wallpaper>() {

    private val wallpaperDao: WallpapersDao = database.wallpaperDao()
    private val searchQueryRemoteKeyDao = database.searchQueryRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Wallpaper>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> WALLPAPERS_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> searchQueryRemoteKeyDao.getRemoteKey(searchQuery).nextPage
        }

        try {
            val response = pexApi.searchWallpapers(searchQuery, page)
            delay(3000)
            val searchServerResults = response.wallpaperList

            val favoriteWallpapers = wallpaperDao.getAllFavorites().first()
            val searchResultWallpapers = searchServerResults.map { serverSearchResultWallpaper ->
                val isFavorite = favoriteWallpapers.any { favoriteWallpaper ->
                    favoriteWallpaper.id == serverSearchResultWallpaper.id
                }

                TypeConverter.wallpaperDtoToWallpaper(serverSearchResultWallpaper, isFavorite)
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    wallpaperDao.deleteSearchResultsForQuery(searchQuery)
                }

                val lastQueryPosition = wallpaperDao.getLastQueryPosition(searchQuery) ?: 0
                var queryPosition = lastQueryPosition + 1

                val searchResults = searchResultWallpapers.map { wallpaper ->
                    TypeConverter.wallpaperToSearchResult(searchQuery, wallpaper, queryPosition++)
                }

                val nextPageKey = page + 1
                wallpaperDao.insertWallpapers(searchResultWallpapers)
                wallpaperDao.insertSearchResults(searchResults)
                searchQueryRemoteKeyDao.insertRemoteKey(
                    SearchQueryRemoteKey(searchQuery, nextPageKey)
                )
            }

            return MediatorResult.Success(endOfPaginationReached = searchServerResults.isEmpty())
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}