package com.adwi.pexwallpapers.ui.wallpapers

import android.app.Application
import com.adwi.pexwallpapers.base.BaseViewModel
import com.adwi.pexwallpapers.data.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    app: Application,
    repository: WallpaperRepository
) : BaseViewModel(app)