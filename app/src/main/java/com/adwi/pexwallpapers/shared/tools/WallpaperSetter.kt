package com.adwi.pexwallpapers.shared.tools

import android.app.Activity
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.util.showToast
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException


class WallpaperSetter(private val activity: Activity, private val setHomeScreen: Boolean) {

    private lateinit var wallpaperManager: WallpaperManager

    companion object {
        private const val TAG = "WallpaperSetter"
    }

    suspend fun setWallpaperByImagePath(imageURL: String) {
        coroutineScope {
            launch {
                val bitmap = getImageUsingCoil(imageURL)
                setWallpaper(bitmap, setHomeScreen)
            }
        }
    }

    private suspend fun getImageUsingCoil(imageURL: String): Bitmap {
        val loader = ImageLoader(activity)
        val request = ImageRequest.Builder(activity)
            .data(imageURL)
            .allowHardware(false)
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    private fun setWallpaper(bitmap: Bitmap, setHomeScreen: Boolean) {
        wallpaperManager = WallpaperManager.getInstance(activity)
        try {
            if (setHomeScreen) setHomeScreenWallpaper(bitmap) else setLockScreenWallpaper(bitmap)
        } catch (ex: IOException) {
            Timber.tag(TAG).d { "Exception: ${ex.printStackTrace()}" }
        }
    }

    private fun setHomeScreenWallpaper(bitmap: Bitmap) {
        wallpaperManager.setBitmap(bitmap)
        showToast(activity, activity.getString(R.string.wallpaper_set))
    }

    private fun setLockScreenWallpaper(bitmap: Bitmap) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                showToast(activity, activity.getString(R.string.wallpaper_set))
            } else {
                showToast(
                    activity,
                    activity.getString(R.string.lock_screen_wallpaper_not_supported)
                )
            }
        } catch (e: Exception) {
            e.message?.let { showToast(activity, it) }
        }
    }
}