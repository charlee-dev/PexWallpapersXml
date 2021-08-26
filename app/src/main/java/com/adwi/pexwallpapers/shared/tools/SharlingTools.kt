package com.adwi.pexwallpapers.shared.tools

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SharingTools(private val context: Context) {

    suspend fun share(imageUrl: String, photographer: String) {

        val uri = saveImageToInternalStorage(imageUrl)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(uri, "image/*")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Picture by $photographer")
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(context, Intent.createChooser(shareIntent, "Choose an app"), null)
    }

    private suspend fun saveImageToInternalStorage(url: String): Uri {
        val bitmap = getBitmap(url)
        try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()
            val stream =
                FileOutputStream("$cachePath/image.png")
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val imagePath = File(context.cacheDir, "images")
        val newFile = File(imagePath, "image.png")
        return FileProvider.getUriForFile(context, "com.example.myapp.fileprovider", newFile)
    }

    private suspend fun getBitmap(url: String): Bitmap {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
}