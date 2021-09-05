package com.adwi.pexwallpapers.shared.tools

import android.app.WallpaperManager
import android.content.Context
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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private const val TAG = "WallpaperSetter"

class WallpaperSetter @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private lateinit var wallpaperManager: WallpaperManager

    suspend fun setWallpaperByImagePath(
        imageURL: String,
        setHomeScreen: Boolean = false,
        setLockScreen: Boolean = false
    ) {
        coroutineScope {
            launch {
                val bitmap = getImageUsingCoil(imageURL)
                setWallpaper(bitmap, setHomeScreen, setLockScreen)
            }
        }
    }

    private suspend fun getImageUsingCoil(imageURL: String): Bitmap {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageURL)
            .allowHardware(false)
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    private fun setWallpaper(bitmap: Bitmap, setHomeScreen: Boolean, setLockScreen: Boolean) {
        wallpaperManager = WallpaperManager.getInstance(context)
        try {
            if (setHomeScreen && !setLockScreen) setHomeScreenWallpaper(bitmap)
            if (!setHomeScreen && setLockScreen) setLockScreenWallpaper(bitmap)
            if (setHomeScreen && setLockScreen) setHomeAndLockScreenWallpaper(bitmap)
        } catch (ex: IOException) {
            Timber.tag(TAG).d { "Exception: ${ex.printStackTrace()}" }
        }
    }

    private fun setHomeScreenWallpaper(
        bitmap: Bitmap
    ) {
        wallpaperManager.setBitmap(bitmap)

    }

    private fun setLockScreenWallpaper(bitmap: Bitmap) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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

    private fun setHomeAndLockScreenWallpaper(bitmap: Bitmap) {
        Timber.tag(TAG).d { "setHomeAndLockScreenWallpaper" }
        setHomeScreenWallpaper(bitmap)
        setLockScreenWallpaper(bitmap)
    }
}