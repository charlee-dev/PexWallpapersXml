package com.adwi.pexwallpapers.data.repository

import com.adwi.pexwallpapers.data.local.WallpapersDao
import com.adwi.pexwallpapers.data.remote.PexApi
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class WallpaperRepository @Inject constructor(
    private val pexApi: PexApi,
    private val wallpapersDao: WallpapersDao
) : Repository