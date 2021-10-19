package com.adwi.pexwallpapers.shared.tools.wallpaper

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import com.adwi.pexwallpapers.shared.tools.permissions.PermissionTools
import com.adwi.pexwallpapers.util.showToast
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


private const val TAG = "WallpaperSetter"

/**
 * Wallpaper setter
 *
 * @property context
 * @property wallpaperManager
 * @constructor Create empty Wallpaper setter
 */
@SuppressLint("NewApi")
class WallpaperSetter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val wallpaperManager: WallpaperManager,
    private val permissionTools: PermissionTools
) {
    /**
     * Set wallpaper
     *
     * @param bitmap
     * @param setHomeScreen
     * @param setLockScreen
     */
    fun setWallpaper(bitmap: Bitmap, setHomeScreen: Boolean, setLockScreen: Boolean) {
        try {
            if (setHomeScreen && !setLockScreen) setHomeScreenWallpaper(bitmap)
            if (!setHomeScreen && setLockScreen) setLockScreenWallpaper(bitmap)
            if (setHomeScreen && setLockScreen) {
                setHomeScreenWallpaper(bitmap)
                setLockScreenWallpaper(bitmap)
            }
        } catch (ex: IOException) {
            Timber.tag(TAG).d("Exception: ${ex.printStackTrace()}")
        }
    }

    /**
     * Set home screen wallpaper
     *
     * @param bitmap
     */
    private fun setHomeScreenWallpaper(
        bitmap: Bitmap
    ) {
        wallpaperManager.setBitmap(bitmap)
    }

    /**
     * Set lock screen wallpaper
     *
     * @param bitmap
     */
    private fun setLockScreenWallpaper(bitmap: Bitmap) {
        try {
            if (permissionTools.runningNOrLater) {
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


    @SuppressLint("MissingPermission")
    fun getCurrentWallpaperForBackup(): Bitmap? {
        var bitmap: Bitmap? = null

        permissionTools.storagePermissionsCheck {
            bitmap = wallpaperManager
                .drawable
                .toBitmap()
        }
        return bitmap
    }
}