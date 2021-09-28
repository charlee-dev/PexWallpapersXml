package com.adwi.pexwallpapers.shared.tools.wallpaper

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import com.adwi.pexwallpapers.shared.tools.permissions.PermissionTools
import com.adwi.pexwallpapers.util.showToast
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

private const val TAG = "WallpaperSetter"

@SuppressLint("NewApi")
class WallpaperSetter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageTools: ImageTools
): WallpaperSetterInterface {

    private lateinit var wallpaperManager: WallpaperManager

    override suspend fun setWallpaperByImagePath(
        imageURL: String,
        setHomeScreen: Boolean,
        setLockScreen: Boolean
    ) {
        val bitmap = imageTools.getImageUsingCoil(imageURL)
        setWallpaper(bitmap, setHomeScreen, setLockScreen)
    }

    override fun setWallpaper(bitmap: Bitmap, setHomeScreen: Boolean, setLockScreen: Boolean) {
        wallpaperManager = WallpaperManager.getInstance(context)
        try {
            if (setHomeScreen && !setLockScreen) setHomeScreenWallpaper(bitmap)
            if (!setHomeScreen && setLockScreen) setLockScreenWallpaper(bitmap)
            if (setHomeScreen && setLockScreen) setHomeAndLockScreenWallpaper(bitmap)
        } catch (ex: IOException) {
            Timber.tag(TAG).d { "Exception: ${ex.printStackTrace()}" }
        }
    }

    override fun setHomeScreenWallpaper(
        bitmap: Bitmap
    ) {
        wallpaperManager.setBitmap(bitmap)
    }

    override fun setLockScreenWallpaper(bitmap: Bitmap) {
        try {
            if (PermissionTools.runningNOrLater) {
                wallpaperManager.setBitmap(
                    bitmap, null, true,
                    WallpaperManager.FLAG_LOCK
                )
            } else {
                showToast(
                    context,
                    context.getString(R.string.lock_screen_wallpaper_not_supported)
                )
            }
        } catch (e: Exception) {
            e.message?.let { showToast(context, it) }
        }
    }

    override fun setHomeAndLockScreenWallpaper(bitmap: Bitmap) {
        Timber.tag(TAG).d { "setHomeAndLockScreenWallpaper" }
        setHomeScreenWallpaper(bitmap)
        setLockScreenWallpaper(bitmap)
    }
}