package com.adwi.pexwallpapers.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.Suggestion
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.remote.PexApi
import com.adwi.pexwallpapers.util.Constants.Companion.PAGING_MAX_SIZE
import com.adwi.pexwallpapers.util.Constants.Companion.PAGING_SIZE
import com.adwi.pexwallpapers.util.Resource
import com.adwi.pexwallpapers.util.TypeConverter
import com.adwi.pexwallpapers.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WallpaperRepository @Inject constructor(
    private val pexApi: PexApi,
    private val wallpapersDatabase: WallpaperDatabase
) {

    private val wallpaperDao = wallpapersDatabase.wallpaperDao()
    private val searchDao = wallpapersDatabase.searchDao()
    private val favoritesDao = wallpapersDatabase.favoritesDao()
    private val curatedDao = wallpapersDatabase.curatedDao()
    private val suggestionsDao = wallpapersDatabase.suggestionsDao()

    // Curated --------------------------------------------------------------------

    fun getCuratedWallpapers(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchRemoteFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Wallpaper>>> =
        networkBoundResource(
            query = {
                curatedDao.getAllCuratedWallpapers()
            },
            fetch = {
                val response = pexApi.getCuratedWallpapers()
                response.wallpaperList
            },
            saveFetchResult = { remoteWallpaperList ->
                // first() collects one list than cancels flow
                val favoriteWallpapers = favoritesDao.getAllFavorites().first()

                val wallpaperList =
                    remoteWallpaperList.map { wallpaper ->
                        val isFavorite = favoriteWallpapers.any { favoriteWallpaper ->
                            favoriteWallpaper.id == wallpaper.id
                        }
                        TypeConverter.wallpaperDtoToWallpaper(wallpaper, "Curated", isFavorite)
                    }

                val curatedWallpapers = wallpaperList.map { wallpaper ->
                    TypeConverter.wallpaperToCuratedWallpaper(wallpaper)
                }

                wallpapersDatabase.withTransaction {
                    curatedDao.deleteAllCuratedWallpapers()
                    wallpaperDao.insertWallpapers(wallpaperList)
                    curatedDao.insertCuratedWallpapers(curatedWallpapers)
                }
            },
            shouldFetch = { wallpapers ->
                if (forceRefresh) {
                    true
                } else {
                    val sortedWallpapers = wallpapers.sortedBy { wallpaper ->
                        wallpaper.updatedAt
                    }
                    val oldestTimestamp = sortedWallpapers.firstOrNull()?.updatedAt
                    val needsRefresh = oldestTimestamp == null ||
                            oldestTimestamp < System.currentTimeMillis() -
                            java.util.concurrent.TimeUnit.MINUTES.toMillis(5)
                    needsRefresh
                }
            },
            onFetchSuccess = onFetchSuccess,
            onFetchFailed = { t ->
                if (t !is HttpException && t !is IOException) {
                    throw t
                }
                onFetchRemoteFailed(t)
            }
        )

    // Favorites --------------------------------------------------------------------

    fun getAllFavorites() = favoritesDao.getAllFavorites()

    suspend fun deleteNonFavoriteWallpapersOlderThan(timestampInMillis: Long) =
        favoritesDao.deleteNonFavoriteWallpapersOlderThan(timestampInMillis)

    suspend fun updateWallpaper(wallpaper: Wallpaper) =
        favoritesDao.updateWallpaperFavorite(wallpaper)

    suspend fun resetAllFavorites() = favoritesDao.resetAllFavorites()

    // Search --------------------------------------------------------------------

    fun getSearchResultsPaged(query: String): Flow<PagingData<Wallpaper>> =
        Pager(
            config = PagingConfig(pageSize = PAGING_SIZE, maxSize = PAGING_MAX_SIZE),
            remoteMediator = SearchNewsRemoteMediator(query, pexApi, wallpapersDatabase),
            pagingSourceFactory = { searchDao.getSearchResultWallpaperPaged(query) }
        ).flow

    // Suggestions

    fun getAllSuggestions() = suggestionsDao.getAllSuggestions()

    suspend fun insertSuggestion(suggestion: Suggestion) {
        suggestionsDao.insertSuggestion(suggestion)
    }

    suspend fun insertAllSuggestions(suggestions: List<Suggestion>) {
        suggestionsDao.insertAllSuggestions(suggestions)
    }

    suspend fun deleteSuggestion(name: String) = suggestionsDao.deleteSuggestion(name)

    // PreviewBottomSheet

    suspend fun getWallpapersByCategory(categoryName: String) =
        wallpaperDao.getWallpapersOfCategory(categoryName)
}