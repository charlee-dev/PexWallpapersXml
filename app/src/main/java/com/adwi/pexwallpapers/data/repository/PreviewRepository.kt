package com.adwi.pexwallpapers.data.repository

import com.adwi.pexwallpapers.base.BaseRepository
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.remote.PexApi
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class PreviewRepository @Inject constructor(
    private val pexApi: PexApi,
    private val wallpapersDatabase: WallpaperDatabase
) : BaseRepository() {

    private val dao = wallpapersDatabase.wallpaperDao()


}