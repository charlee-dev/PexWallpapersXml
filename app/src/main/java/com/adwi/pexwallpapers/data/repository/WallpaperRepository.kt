package com.adwi.pexwallpapers.data.repository

import androidx.room.withTransaction
import com.adwi.pexwallpapers.data.TypeConverter
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.CuratedWallpapers
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.remote.PexApi
import com.adwi.pexwallpapers.util.Resource
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

    private val dao = wallpapersDatabase.wallpaperDao()

    fun getCuratedWallpapers(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchRemoteFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Wallpaper>>> =
        networkBoundResource(
            query = {
                dao.getAllCuratedWallpapers()
            },
            fetch = {
                val response = pexApi.getCuratedPhotos()
                response.wallpaperList
            },
            saveFetchResult = { remoteWallpaperList ->
                // first() collects one list than cancels flow
                val favoriteWallpapers = dao.getAllFavorites().first()

                val wallpaperList =
                    remoteWallpaperList.map { wallpaper ->
                        val isFavorite = favoriteWallpapers.any { favoriteWallpaper ->
                            favoriteWallpaper.id == wallpaper.id
                        }
                        TypeConverter.wallpaperDtoToWallpaper(wallpaper, isFavorite)
                    }

                val curatedWallpapers = wallpaperList.map { wallpaper ->
                    CuratedWallpapers(wallpaper.id)
                }

                wallpapersDatabase.withTransaction {
                    dao.deleteAllCuratedWallpapers()
                    dao.insertWallpapers(wallpaperList)
                    dao.insertCuratedWallpapers(curatedWallpapers)
                }
            },
            shouldFetch = { cachedArticles ->
                if (forceRefresh) {
                    true
                } else {
                    val sortedArticles = cachedArticles.sortedBy { article ->
                        article.updatedAt
                    }
                    val oldestTimestamp = sortedArticles.firstOrNull()?.updatedAt
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

    fun getAllFavorites(): Flow<List<Wallpaper>> =
        dao.getAllFavorites()

    suspend fun deleteNonFavoriteWallpapersOlderThan(timestampInMillis: Long) =
        dao.deleteNonFavoriteWallpapersOlderThan(timestampInMillis)

    suspend fun updateWallpaper(wallpaper: Wallpaper) =
        dao.updateWallpaperFavorite(wallpaper)

    suspend fun resetAllFavorites() =
        dao.resetAllFavorites()
}