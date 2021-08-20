package com.adwi.pexwallpapers.ui.preview

import com.adwi.pexwallpapers.data.WallpaperRepository
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import com.adwi.pexwallpapers.util.onIO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(private val repository: WallpaperRepository) :
    BaseViewModel() {

    fun favoriteOnDoubleClicked(wallpaper: Wallpaper) {
        if (!wallpaper.isFavorite) {
            val updatedWallpaper = wallpaper.copy(isFavorite = true)
            onIO {
                repository.updateWallpaper(updatedWallpaper)
            }
        }
    }
}