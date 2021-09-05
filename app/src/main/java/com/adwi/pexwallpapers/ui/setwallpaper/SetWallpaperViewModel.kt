package com.adwi.pexwallpapers.ui.setwallpaper

import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import com.adwi.pexwallpapers.di.IoDispatcher
import com.adwi.pexwallpapers.shared.tools.SharingTools
import com.adwi.pexwallpapers.shared.tools.WallpaperSetter
import com.adwi.pexwallpapers.ui.base.BaseViewModel
import com.adwi.pexwallpapers.util.onDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class SetWallpaperViewModel @Inject constructor(
    private val repository: WallpaperRepositoryInterface,
    private val sharingTools: SharingTools,
    private val wallpaperSetter: WallpaperSetter,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    fun onFavoriteClick(wallpaper: Wallpaper) {
        val isFavorite = wallpaper.isFavorite
        wallpaper.isFavorite = !isFavorite
        onDispatcher(ioDispatcher) {
            repository.updateWallpaper(wallpaper)
        }
    }

    fun goToPexels(wallpaper: Wallpaper) {
        sharingTools.openUrlInBrowser(wallpaper.url!!)
    }

    fun shareWallpaper(wallpaper: Wallpaper) {
        onDispatcher(ioDispatcher) {
            sharingTools.shareImage(
                wallpaper.imageUrl,
                wallpaper.photographer
            )
        }
    }

    fun downloadWallpaper(wallpaper: Wallpaper) {
        onDispatcher(ioDispatcher) {
            sharingTools.saveImageLocally(
                wallpaper.imageUrl,
                wallpaper.photographer
            )
        }
    }

    fun setWallpaper(imageURL: String, setHomeScreen: Boolean, setLockScreen: Boolean) {
        onDispatcher(ioDispatcher) {
            wallpaperSetter.setWallpaperByImagePath(
                imageURL = imageURL,
                setHomeScreen = setHomeScreen,
                setLockScreen = setLockScreen
            )
        }
    }
}