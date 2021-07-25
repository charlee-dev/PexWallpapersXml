package com.adwi.pexwallpapers.data.repository

import com.adwi.pexwallpapers.data.local.WallpapersDao
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class PreviewRepository @Inject constructor(
    private val wallpapersDao: WallpapersDao
) : Repository