package com.adwi.pexwallpapers.shared.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result.failure
import androidx.work.ListenableWorker.Result.success
import androidx.work.WorkerParameters
import com.adwi.pexwallpapers.data.repository.WallpaperRepository
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import com.adwi.pexwallpapers.shared.tools.notification.Channel
import com.adwi.pexwallpapers.shared.tools.notification.NotificationTools
import com.adwi.pexwallpapers.shared.tools.wallpaper.WallpaperSetter
import com.adwi.pexwallpapers.util.Constants.Companion.WALLPAPER_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

private const val TAG = "AutoChangeWallpaperWork"

/**
 * Auto change wallpaper work
 *
 * @property notificationTools
 * @property imageTools
 * @property wallpaperSetter
 * @constructor
 *
 * @param context
 * @param params
 */
@HiltWorker
class AutoChangeWallpaperWork @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val wallpaperRepository: WallpaperRepository,
    private val notificationTools: NotificationTools,
    private val imageTools: ImageTools,
    private val wallpaperSetter: WallpaperSetter
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {

            // Get arguments
            val wallpaperId = inputData.getInt(WALLPAPER_ID, 0)

            // Get wallpaper from repository
            val wallpaper = wallpaperRepository.getWallpaperById(wallpaperId)

            // Get current wallpaper for backup
            val backupImage = wallpaperSetter.getCurrentWallpaperForBackup()

            // Save backup locally
            if (backupImage != null) {
                imageTools.backupImageToLocal(wallpaperId, backupImage)
            } else {
                Timber.tag(TAG).d("doWork - No backup bitmap")
            }

            // Fetch bitmap using Coil
            val bitmap = wallpaper.src?.let { imageTools.getBitmapFromRemote(it.portrait) }

            // Set wallpaper
            if (bitmap != null) {
                wallpaperSetter.setWallpaper(bitmap, setHomeScreen = true, setLockScreen = false)

                notificationTools.createNotificationForWallpaper(
                    channelId = Channel.NEW_WALLPAPER,
                    bitmap = bitmap,
                    wallpaperId = wallpaperId,
                    categoryName = wallpaper.categoryName,
                    photographerName = wallpaper.photographer
                )
            } else {
                Timber.tag(TAG).d("odWork - bitmap null")
            }

            Timber.tag(TAG).d("doWork - success")
            success()

            success()
        } catch (ex: Exception) {
            Timber.tag(TAG).d(ex.toString())
            failure()
        }
    }
}