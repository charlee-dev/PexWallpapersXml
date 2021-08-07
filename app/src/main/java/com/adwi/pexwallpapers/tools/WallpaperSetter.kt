package com.adwi.pexwallpapers.tools

import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException


class WallpaperSetter(private val context: Context) {

    suspend fun setWallpaperByImagePath(activity: Activity, imageURL: String) {

        val loader = ImageLoader(activity)
        val request = ImageRequest.Builder(activity)
            .data(imageURL)
            .allowHardware(false)
            .build()
        coroutineScope {
            launch {
                val result = (loader.execute(request) as SuccessResult).drawable
                val bitmap = (result as BitmapDrawable).bitmap

                val wallpaperManager = WallpaperManager.getInstance(context)
                try {
                    Timber.tag("WallpaperSetter").d { "Bitmap: $bitmap" }
                    wallpaperManager.setBitmap(bitmap)
                } catch (ex: IOException) {
                    Timber.tag("WallpaperSetter").d { "Ex: ${ex.printStackTrace()}" }
                }
            }
        }
    }
}