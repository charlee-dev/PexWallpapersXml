package com.adwi.pexwallpapers.data.repository

import androidx.room.withTransaction
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.remote.PexApi
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import com.adwi.pexwallpapers.util.TypeConverter
import com.adwi.pexwallpapers.util.networkBoundResource
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WallpaperRepository @Inject constructor(
    private val pexApi: PexApi,
    private val wallpapersDatabase: WallpaperDatabase
) : WallpaperRepositoryInterface {

    private val wallpaperDao = wallpapersDatabase.wallpaperDao()
    private val curatedDao = wallpapersDatabase.curatedDao()
    private val favoritesDao = wallpapersDatabase.favoritesDao()

    override fun getCuratedWallpapers(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchRemoteFailed: (Throwable) -> Unit
    ) =
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

    override fun getWallpapersByCategory(categoryName: String) =
        wallpaperDao.getWallpapersOfCategory(categoryName)

    override suspend fun updateWallpaper(wallpaper: Wallpaper) =
        wallpaperDao.updateWallpaperFavorite(wallpaper)
}