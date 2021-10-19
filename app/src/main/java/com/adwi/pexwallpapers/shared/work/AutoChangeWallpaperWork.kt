package com.adwi.pexwallpapers.shared.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result.failure
import androidx.work.ListenableWorker.Result.success
import androidx.work.WorkerParameters
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import com.adwi.pexwallpapers.shared.tools.notification.Channel
import com.adwi.pexwallpapers.shared.tools.notification.NotificationTools
import com.adwi.pexwallpapers.shared.tools.wallpaper.WallpaperSetter
import com.adwi.pexwallpapers.util.Constants.Companion.WORKER_AUTO_WALLPAPER_IMAGE_URL_FULL
import com.adwi.pexwallpapers.util.Constants.Companion.WORKER_AUTO_WALLPAPER_NOTIFICATION_IMAGE
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

private const val TAG = "AutoChangeWallpaperWork"

@HiltWorker
class AutoChangeWallpaperWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val notificationTools: NotificationTools,
    private val imageTools: ImageTools,
    private val wallpaperSetter: WallpaperSetter
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Get arguments
            val wallpaperImage = inputData.getString(WORKER_AUTO_WALLPAPER_IMAGE_URL_FULL)
            val notificationImage = inputData.getString(WORKER_AUTO_WALLPAPER_NOTIFICATION_IMAGE)
            Timber.tag(TAG).d("doWork - $wallpaperImage, $notificationImage")
            if (wallpaperImage != null && notificationImage != null) {

                // Get bitmap
                val bitmap = imageTools.getBitmapFromRemote(wallpaperImage)

                // Set wallpaper
                wallpaperSetter.setWallpaper(bitmap, setHomeScreen = true, setLockScreen = false)

                //Send notification
                val id = inputData.getLong(wallpaperImage, 0).toInt()
                notificationTools.sendNotification(
                    id = id,
                    channel = Channel.NEW_WALLPAPER,
                    imageUrl = notificationImage
                )

                Timber.tag(TAG).d("doWork - success")
                success()
            } else {
                Timber.tag(TAG).d("wallpaper or notificationImage is null")
                failure()
            }
            success()
        } catch (ex: Exception) {
            Timber.tag(TAG).d(ex.toString())
            failure()
        }
    }
}