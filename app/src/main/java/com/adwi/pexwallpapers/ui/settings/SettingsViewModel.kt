package com.adwi.pexwallpapers.ui.settings

import android.content.Context
import androidx.work.*
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Settings
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.local.entity.defaultSettings
import com.adwi.pexwallpapers.data.repository.FavoritesRepository
import com.adwi.pexwallpapers.data.repository.interfaces.SettingsRepositoryInterface
import com.adwi.pexwallpapers.di.IoDispatcher
import com.adwi.pexwallpapers.shared.tools.NotificationTools
import com.adwi.pexwallpapers.shared.tools.SharingTools
import com.adwi.pexwallpapers.shared.work.AutoChangeWallpaperWork
import com.adwi.pexwallpapers.shared.work.WORKER_NEW_WALLPAPER_IMAGE_URL_FULL
import com.adwi.pexwallpapers.shared.work.WORKER_NEW_WALLPAPER_NOTIFICATION_IMAGE
import com.adwi.pexwallpapers.ui.base.BaseViewModel
import com.adwi.pexwallpapers.util.onDispatcher
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepositoryInterface,
    private val favoritesRepository: FavoritesRepository,
    private val sharingTools: SharingTools,
    notificationTools: NotificationTools,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext context: Context
) : BaseViewModel() {

    private val application = getApplication(context)
    private val workManager = WorkManager.getInstance(application)

    val currentChangePeriodType =
        MutableStateFlow(R.id.hours_radio_button)

    private val _currentSettings = MutableStateFlow(defaultSettings)
    val currentSettings: StateFlow<Settings> = _currentSettings

    private val favorites = MutableStateFlow<List<Wallpaper>>(emptyList())

    init {
        notificationTools.setupNotifications()
        onDispatcher(ioDispatcher) { _currentSettings.value = repository.getSettings() }
        onDispatcher(ioDispatcher) {
            favorites.value = favoritesRepository.getAllFavorites().first()
        }
    }

    fun updatePushNotification(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updatePushNotification(enabled) }
        Timber.tag(TAG).d { "updatePushNotification $enabled" }
    }

    fun updateNewWallpaperSet(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateNewWallpaperSet(enabled) }
        Timber.tag(TAG).d { "updateNewWallpaperSet $enabled" }
    }

    fun updateWallpaperRecommendations(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateWallpaperRecommendations(enabled) }
        Timber.tag(TAG).d { "updateWallpaperRecommendations $enabled" }
    }

    fun updateAutoChangeWallpaper(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateAutoChangeWallpaper(enabled) }
        Timber.tag(TAG).d { "updateAutoChangeWallpaper $enabled" }

    }

    fun updateDownloadOverWiFi(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateDownloadOverWiFi(enabled) }
        Timber.tag(TAG).d { "updateDownloadOverWiFi $enabled" }
    }

    fun updateChangePeriodType(radioButton: Int) {
        currentChangePeriodType.value = radioButton
        onDispatcher(ioDispatcher) { repository.updateChangePeriodType(radioButton) }
        Timber.tag(TAG).d { "updateChangePeriodType $radioButton" }
    }

    fun updateChangePeriodValue(periodValue: Float) {
        onDispatcher(ioDispatcher) { repository.updateChangePeriodValue(periodValue) }
        Timber.tag(TAG).d { "updateChangePeriodValue $periodValue" }
    }

    fun resetSettings() {
        onDispatcher(ioDispatcher) { repository.resetAllSettings() }
        Timber.tag(TAG).d { "resetSettings" }
    }

    fun contactSupport() {
        sharingTools.contactSupport()
    }

    private fun createDataForAutoChangeWallpaperWorker(wallpaper: Wallpaper): Data {
        val builder = Data.Builder()
        builder.putString(WORKER_NEW_WALLPAPER_IMAGE_URL_FULL, wallpaper.src?.portrait)
        builder.putString(WORKER_NEW_WALLPAPER_NOTIFICATION_IMAGE, wallpaper.src?.portrait)
        Timber.tag(TAG).d { "createDataForAutoChangeWallpaperWorker" }
        return builder.build()
    }

    fun saveSettings() {
        onDispatcher(ioDispatcher) {
            setupAllWorks()
        }
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

    fun setupAllWorks() {
        Timber.tag(TAG).d { "setupAllWorks" }

        // Get current settings
        val settings = currentSettings.value
        val timeUnit = getTimeUnit(settings.selectedButton)
        val sliderValue = settings.sliderValue.toLong()
        var repeatValue = sliderValue * favorites.value.size
        if (timeUnit == TimeUnit.MINUTES && repeatValue < 15) {
            repeatValue = 15
        }

        // Auto change wallpaper
        if (settings.autoChangeWallpaper) {
            Timber.tag(TAG).d { "setupAllWorks - autoChangeWallpaper - true" }
            Timber.tag(TAG).d { "favorites list size = ${favorites.value.size}" }
            workManager.cancelAllWorkByTag(TAG_NEW_WALLPAPER)
            favorites.value.forEachIndexed { index, wallpaper ->
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

    override fun onCleared() {
        Timber.tag(TAG).d { "onCleared" }
        setupAllWorks()
        super.onCleared()
    }
}

private const val TAG_NEW_WALLPAPER = "tag_new_wallpaper"