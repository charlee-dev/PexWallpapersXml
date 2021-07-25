package com.adwi.pexwallpapers.data.repository

import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.remote.PexApi
import javax.inject.Inject

class PreviewRepository @Inject constructor(
    private val pexApi: PexApi,
    private val wallpapersDatabase: WallpaperDatabase
) {

    private val dao = wallpapersDatabase.wallpaperDao()


}