package com.adwi.pexwallpapers.shared.work

import android.content.Context
import androidx.work.*
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import timber.log.Timber
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.adwi.pexwallpapers.R

class WorkTools @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val application = Contexts.getApplication(context)
    private val workManager = WorkManager.getInstance(application)

    fun setupAllWorks(favorites: List<Wallpaper>, timeUnit: TimeUnit, timeValue: Float) {
        Timber.tag(TAG).d ("setupAllWorks")

        // Get current settings
        var repeatValue = timeValue.toLong() * favorites.size
        if (timeUnit == TimeUnit.MINUTES && repeatValue < 15f) {
            repeatValue = 15
        }

        // Auto change wallpaper
        Timber.tag(TAG).d ("setupAllWorks - autoChangeWallpaper - true")
        Timber.tag(TAG).d ("favorites list size = ${favorites.size}")
        workManager.cancelAllWorkByTag(TAG_NEW_WALLPAPER)

        favorites.forEachIndexed { index, wallpaper ->
            Timber.tag(TAG)
                .d ("setupAllWorks - wallpaperPhotographer = ${wallpaper.photographer}")

            val delay = timeValue.toLong() * (index + 1) / 15

            createAutoChangeWallpaperWork(
                wallpaper,
                timeUnit,
                delay,
                repeatValue
            )
        }
    }

    private fun createDataForAutoChangeWallpaperWorker(wallpaper: Wallpaper): Data {
        val builder = Data.Builder()
        builder.putString(WORKER_NEW_WALLPAPER_IMAGE_URL_FULL, wallpaper.src?.portrait)
        builder.putString(WORKER_NEW_WALLPAPER_NOTIFICATION_IMAGE, wallpaper.src?.portrait)
        Timber.tag(TAG).d ("createDataForAutoChangeWallpaperWorker")
        return builder.build()
    }

    private fun createAutoChangeWallpaperWork(
        wallpaper: Wallpaper,
        delayTimeUnit: TimeUnit,
        delay: Long,
        repeatInterval: Long
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()
        val work = PeriodicWorkRequestBuilder<AutoChangeWallpaperWork>(
            repeatInterval = repeatInterval,
            repeatIntervalTimeUnit = delayTimeUnit
        )
            .setInputData(createDataForAutoChangeWallpaperWorker(wallpaper))
            .setInitialDelay(delay, delayTimeUnit)
//            .setConstraints(constraints)
            .addTag(TAG_NEW_WALLPAPER)
            .build()

        WorkManager.getInstance(this.application.applicationContext).enqueueUniquePeriodicWork(
            "MyUniqueWorkName",
            ExistingPeriodicWorkPolicy.REPLACE,
            work
        )

        Timber.tag(TAG)
            .d ("Created work: wallpaperId = ${wallpaper.id}, delay = $delay, delayTimeUnit = $delayTimeUnit, repeatInterval = $repeatInterval")
        workManager.enqueue(work)
    }
}

const val TAG_NEW_WALLPAPER = "tag_new_wallpaper"