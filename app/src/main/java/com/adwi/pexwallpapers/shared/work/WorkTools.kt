package com.adwi.pexwallpapers.shared.work

import android.content.Context
import androidx.work.*
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.FavoritesRepository
import com.adwi.pexwallpapers.data.repository.SettingsRepository
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG_NEW_WALLPAPER = "tag_new_wallpaper"

class WorkTools @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
    private val favoritesRepository: FavoritesRepository
) {

    private val application = Contexts.getApplication(context)
    private val workManager = WorkManager.getInstance(application)

    suspend fun setupAllWorks() {
        Timber.tag(TAG).d { "setupAllWorks" }

        // Get current settings and list of favorites
        val settings = settingsRepository.getSettings()
        val favorites = favoritesRepository.getAllFavorites().first()

        // Getting setting values
        val timeUnit = getTimeUnit(settings.selectedButton)
        val sliderValue = settings.sliderValue.toLong()
        var repeatValue = sliderValue * favorites.size
        if (timeUnit == TimeUnit.MINUTES && repeatValue < 15) {
            repeatValue = 15
        }

        // Auto change wallpaper
        if (settings.autoChangeWallpaper) {
            Timber.tag(TAG).d { "setupAllWorks - autoChangeWallpaper - true" }
            Timber.tag(TAG).d { "favorites list size = ${favorites.size}" }
            workManager.cancelAllWorkByTag(TAG_NEW_WALLPAPER)
            favorites.forEachIndexed { index, wallpaper ->
                Timber.tag(TAG)
                    .d { "setupAllWorks - wallpaperPhotographer = ${wallpaper.photographer}" }
                val delay = sliderValue * (index + 1) / 15

                createAutoChangeWallpaperWork(
                    wallpaper,
                    timeUnit,
                    delay,
                    repeatValue
                )
            }
        } else {
            Timber.tag(TAG).d { "autoChangeWallpaper - false" }
            workManager.cancelAllWorkByTag(TAG_NEW_WALLPAPER)
        }
    }

    private fun createDataForAutoChangeWallpaperWorker(wallpaper: Wallpaper): Data {
        Timber.tag(TAG).d { "createDataForAutoChangeWallpaperWorker" }
        val builder = Data.Builder()
        builder.putString(WORKER_NEW_WALLPAPER_IMAGE_URL_FULL, wallpaper.src?.portrait)
        builder.putString(WORKER_NEW_WALLPAPER_NOTIFICATION_IMAGE, wallpaper.src?.portrait)
        return builder.build()
    }

    private fun createAutoChangeWallpaperWork(
        wallpaper: Wallpaper,
        delayTimeUnit: TimeUnit,
        delay: Long,
        repeatInterval: Long
    ) {
        // TODO() Add constrains last think
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
            .d { "Created work: wallpaperId = ${wallpaper.id}, delay = $delay, delayTimeUnit = $delayTimeUnit, repeatInterval = $repeatInterval" }
        workManager.enqueue(work)
    }

    private fun getTimeUnit(durationType: Int): TimeUnit {
        Timber.tag(TAG).d { "getDelayForWork timeunit = $durationType" }
        return when (durationType) {
            R.id.minutes_radio_button -> TimeUnit.MINUTES
            R.id.hours_radio_button -> TimeUnit.HOURS
            R.id.days_radio_button -> TimeUnit.DAYS
            else -> TimeUnit.DAYS // weeks
        }
    }
}