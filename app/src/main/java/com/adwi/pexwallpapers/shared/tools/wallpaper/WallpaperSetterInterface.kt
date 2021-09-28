package com.adwi.pexwallpapers.shared.tools.wallpaper

import android.graphics.Bitmap

interface WallpaperSetterInterface {

    suspend fun setWallpaperByImagePath(
        imageURL: String,
        setHomeScreen: Boolean = false,
        setLockScreen: Boolean = false
    )

    fun setWallpaper(bitmap: Bitmap, setHomeScreen: Boolean, setLockScreen: Boolean)

    fun setHomeScreenWallpaper(bitmap: Bitmap)

    fun setLockScreenWallpaper(bitmap: Bitmap)

    fun setHomeAndLockScreenWallpaper(bitmap: Bitmap)
}