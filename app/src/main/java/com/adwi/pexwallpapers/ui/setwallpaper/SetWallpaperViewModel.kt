package com.adwi.pexwallpapers.ui.setwallpaper

import android.content.Context
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import com.adwi.pexwallpapers.di.IoDispatcher
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import com.adwi.pexwallpapers.shared.tools.sharing.SharingTools
import com.adwi.pexwallpapers.shared.tools.wallpaper.WallpaperSetter
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
    private val imageTools: ImageTools,
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

    fun shareWallpaper(context: Context, wallpaper: Wallpaper) {
        onDispatcher(ioDispatcher) {
            sharingTools.shareImage(
                context,
                wallpaper.imageUrl,
                wallpaper.photographer
            )
        }
    }

    fun downloadWallpaper(wallpaper: Wallpaper) {
        onDispatcher(ioDispatcher) {
            imageTools.saveImageLocally(
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