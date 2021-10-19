package com.adwi.pexwallpapers.shared.tools.image

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.adwi.pexwallpapers.shared.tools.permissions.PermissionTools
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class ImageTools @Inject constructor(
    @ApplicationContext private val context: Context,
    private val permissionTools: PermissionTools
) : ImageToolsInterface {

    override suspend fun getBitmapFromRemote(url: String): Bitmap {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    override suspend fun saveImageToInternalStorage(url: String): Uri? {
        val bitmap = getBitmapFromRemote(url)
        return saveImage(bitmap, context)
    }

    override suspend fun saveImageLocally(imageUrl: String, photographer: String) {
        val bitmap = getBitmapFromRemote(imageUrl)
        MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            photographer,
            "Image of $photographer"
        )
    }

    @SuppressLint("MissingPermission")
    override fun backupCurrentWallpaper() {
        val wallpaperManager = WallpaperManager.getInstance(context)

//        TODO() move this logic to viewModel

        permissionTools.storagePermissionsCheck {
            val bitmap = wallpaperManager
                .drawable
                .toBitmap()
            saveBackupBitmapToLocal(bitmap)
        }
    }

    override fun saveBackupBitmapToLocal(bitmap: Bitmap) {
        MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            "backup_wallpaper",
            "Backup of last set wallpaper"
        )
    }

    override fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("InlinedApi")
    override fun saveImage(bitmap: Bitmap, context: Context): Uri? {
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
}