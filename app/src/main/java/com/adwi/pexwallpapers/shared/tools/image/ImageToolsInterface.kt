package com.adwi.pexwallpapers.shared.tools.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.OutputStream

interface ImageToolsInterface {

    suspend fun saveImageToInternalStorage(url: String): Uri?

    suspend fun getBitmapFromRemote(url: String): Bitmap

    suspend fun saveImageLocally(imageUrl: String, photographer: String)

    fun backupCurrentWallpaper()

    fun saveBackupBitmapToLocal(bitmap: Bitmap)

    fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?)

    fun saveImage(bitmap: Bitmap, context: Context): Uri?
}