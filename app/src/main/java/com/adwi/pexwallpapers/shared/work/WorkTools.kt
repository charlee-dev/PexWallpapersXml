package com.adwi.pexwallpapers.shared.work

import androidx.work.*
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.util.Constants.Companion.WALLPAPER_ID
import com.adwi.pexwallpapers.util.Constants.Companion.WORK_AUTO_WALLPAPER
import com.adwi.pexwallpapers.util.Constants.Companion.WORK_AUTO_WALLPAPER_NAME
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "WorkTools"
private const val timeSpeeding = 30
private const val minutesWorkTimes = 3

class WorkTools @Inject constructor(
    private val workManager: WorkManager
) {
    fun cancelWorks(workTag: String) {
        workManager.cancelAllWorkByTag(workTag)
        Timber.tag(TAG).d("cancelWorks - $WORK_AUTO_WALLPAPER")
    }

    fun setupAutoChangeWallpaperWorks(
        favorites: List<Wallpaper>,
        timeUnit: TimeUnit,
        timeValue: Float
    ) {
        Timber.tag(TAG).d("setupAllWorks")
        cancelWorks(WORK_AUTO_WALLPAPER)

        var multiplier = 1

        for (number in 1..minutesWorkTimes) {
            favorites.forEach { wallpaper ->

                val delay = getDelay(
                    timeUnit = timeUnit,
                    timeValue = timeValue
                ) / timeSpeeding

                createAutoChangeWallpaperWork(
                    workName = "${number}_${WORK_AUTO_WALLPAPER_NAME}_${wallpaper.id}",
                    wallpaper = wallpaper,
                    delay = delay * multiplier
                )
                multiplier++
            }
        }
    }

    private fun createAutoChangeWallpaperWork(
        workName: String,
        wallpaper: Wallpaper,
        delay: Long
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val work = OneTimeWorkRequestBuilder<AutoChangeWallpaperWork>()
            .setInputData(createDataForAutoChangeWallpaperWorker(wallpaper))
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .addTag(WORK_AUTO_WALLPAPER)
            .build()

        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            work
        )

        Timber.tag(TAG)
            .d("Created work: \nwallpaperId = ${wallpaper.id}, \ndelay = ${delay / 1000}s")
    }

    private fun createDataForAutoChangeWallpaperWorker(wallpaper: Wallpaper): Data {
        val builder = Data.Builder()
        builder.putInt(WALLPAPER_ID, wallpaper.id)
        Timber.tag(TAG).d("createDataForAutoChangeWallpaperWorker \nimageUrl = ${wallpaper.src!!.portrait} \nwallpaperId = ${wallpaper.id}")
        return builder.build()
    }

    private fun getDelay(timeUnit: TimeUnit, timeValue: Float): Long {
        val minute = 60 * 1000
        val hour = 60 * minute
        val day = 24 * hour
        val value = timeValue.toInt()
        val delay = when (timeUnit) {
            TimeUnit.MINUTES -> minute * value
            TimeUnit.HOURS -> hour * value
            else -> day * value
        }.toLong()

        return delay
    }
}