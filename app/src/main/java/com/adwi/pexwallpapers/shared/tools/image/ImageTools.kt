package com.adwi.pexwallpapers.shared.tools.image

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.adwi.pexwallpapers.shared.tools.permissions.PermissionTools
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject


class ImageTools @Inject constructor(
    @ApplicationContext private val context: Context,
    private val permissionTools: PermissionTools
) {
    suspend fun fetchRemoteAndSaveLocally(id: Int, url: String): Uri {
        val bitmap = getBitmapFromRemote(url)
        val uri = saveImageToInternalStorage(id, bitmap)
        Timber.tag(TAG).d("fetchRemoteAndSaveLocally - $uri")
        return uri
    }

    suspend fun fetchRemoteAndSaveToGallery(url: String): Uri? {
        val bitmap = getBitmapFromRemote(url)
        val uri = saveImageToGallery(bitmap)
        Timber.tag(TAG).d("fetchRemoteAndSaveToGallery - $uri")
        return uri
    }

    suspend fun getBitmapFromRemote(imageUrl: String): Bitmap {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val drawable = (loader.execute(request) as SuccessResult).drawable
        return (drawable as BitmapDrawable).bitmap
    }

    fun bitmapFromLocal(wallpaperId: Int): Bitmap {
        val directory = ContextWrapper(context).getDir("images", Context.MODE_PRIVATE)
        val file = File(directory, "$BACKUP_WALLPAPER$wallpaperId.jpg")

        if (!file.exists()) {
            Timber.tag(TAG).d("bitmapFromLocal - file doesn't exist")
        }

        return BitmapFactory.decodeFile(file.absolutePath)
    }

    fun deleteBackupBitmap(wallpaperId: Int) {
        Timber.tag(TAG).d("deleteBackupBitmap - Deleted image $wallpaperId")
    }

    // SAVE

    fun backupImageToLocal(wallpaperId: Int, bitmap: Bitmap): Uri {
        val directory = ContextWrapper(context).getDir("images", Context.MODE_PRIVATE)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val fileName = "$BACKUP_WALLPAPER$wallpaperId.jpg"
        val file = File(directory, fileName)

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: Exception) { // Catch the exception
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }


    private fun saveImageToInternalStorage(wallpaperId: Int, bitmap: Bitmap): Uri {
        val directory = ContextWrapper(context).getDir("images", Context.MODE_PRIVATE)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val fileName = "$BACKUP_WALLPAPER$wallpaperId.jpg"
        val file = File(directory, fileName)

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: Exception) { // Catch the exception
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }

    @SuppressLint("InlinedApi")
    fun saveImageToGallery(bitmap: Bitmap): Uri? {
        if (permissionTools.runningQOrLater) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/test_pictures")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "img_${SystemClock.uptimeMillis()}")

            val uri: Uri? =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
                context.contentResolver.update(uri, values, null, null)
                return uri
            }
        } else {
            val directory = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + "pex_wallpapers"
            )
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = "img_${SystemClock.uptimeMillis()}" + ".jpeg"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            if (file.absolutePath != null) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                // .DATA is deprecated in API 29
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                return Uri.fromFile(file)
            }
        }
        return null
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

private const val TAG = "ImageTools"
private const val BACKUP_WALLPAPER = "backup_wallpaper_"