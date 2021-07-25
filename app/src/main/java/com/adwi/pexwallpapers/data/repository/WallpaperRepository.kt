package com.adwi.pexwallpapers.data.repository

import com.adwi.pexwallpapers.data.TypeConverter
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.remote.PexApi
import javax.inject.Inject

class WallpaperRepository @Inject constructor(
    private val pexApi: PexApi,
    private val wallpapersDatabase: WallpaperDatabase
) {

    private val dao = wallpapersDatabase.wallpaperDao()

    suspend fun getCuratedWallpapers(): List<Wallpaper> {
        val response = pexApi.getCuratedPhotos()
        val curatedWallpapers = response.wallpaperList
        return TypeConverter.wallpaperDtoListToEntityListMinimal(curatedWallpapers)
    }
}