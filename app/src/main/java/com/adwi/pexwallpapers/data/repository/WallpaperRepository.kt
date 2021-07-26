package com.adwi.pexwallpapers.data.repository

import androidx.room.withTransaction
import com.adwi.pexwallpapers.data.TypeConverter
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.remote.PexApi
import com.adwi.pexwallpapers.util.Resource
import com.adwi.pexwallpapers.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WallpaperRepository @Inject constructor(
    private val pexApi: PexApi,
    private val wallpapersDatabase: WallpaperDatabase
) {

    private val dao = wallpapersDatabase.wallpaperDao()

    fun getCuratedWallpapers(
        onFetchSuccess: () -> Unit,
        onFetchRemoteFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Wallpaper>>> =
        networkBoundResource(
            query = {
                dao.getAllWallpapers()
            },
            fetch = {
                val response = pexApi.getCuratedPhotos()
                response.wallpaperList
            },
            saveFetchResult = { remoteWallpaperList ->
                val wallpaperList =
                    TypeConverter.wallpaperDtoListToEntityList(remoteWallpaperList)
                wallpapersDatabase.withTransaction {
                    dao.insertWallpapers(wallpaperList)
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
}