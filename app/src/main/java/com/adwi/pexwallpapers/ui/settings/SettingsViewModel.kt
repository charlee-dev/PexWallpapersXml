package com.adwi.pexwallpapers.ui.settings

import android.content.Context
import androidx.work.WorkManager
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Settings
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.FavoritesRepository
import com.adwi.pexwallpapers.data.repository.interfaces.SettingsRepositoryInterface
import com.adwi.pexwallpapers.di.IoDispatcher
import com.adwi.pexwallpapers.shared.tools.notification.NotificationTools
import com.adwi.pexwallpapers.shared.tools.sharing.SharingTools
import com.adwi.pexwallpapers.shared.work.WorkTools
import com.adwi.pexwallpapers.ui.base.BaseViewModel
import com.adwi.pexwallpapers.util.Constants.Companion.WORK_AUTO_WALLPAPER
import com.adwi.pexwallpapers.util.onDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepositoryInterface,
    private val favoritesRepository: FavoritesRepository,
    private val sharingTools: SharingTools,
    private val workTools: WorkTools,
    private val notificationTools: NotificationTools,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val favorites = MutableStateFlow<List<Wallpaper>>(emptyList())

    val settings = repository.getSettings()

    init {
        notificationTools.setupNotifications()
        getFavorites()
    }

    private fun getFavorites() {
        onDispatcher(ioDispatcher) {
            favorites.value = favoritesRepository.getAllFavorites().first()
        }
    }

    fun updatePushNotification(checked: Boolean) {
        onDispatcher(ioDispatcher) { repository.updatePushNotification(checked) }
    }

    fun updateNewWallpaperSet(checked: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateNewWallpaperSet(checked) }
    }

    fun updateWallpaperRecommendations(checked: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateWallpaperRecommendations(checked) }
    }

    fun updateAutoChangeWallpaper(checked: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateAutoChangeWallpaper(checked) }
    }

    fun updateDownloadOverWiFi(checked: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateDownloadOverWiFi(checked) }
    }

    fun updateChangePeriodType(radioButton: Int) {
        onDispatcher(ioDispatcher) { repository.updateChangePeriodType(radioButton) }
    }

    fun updateChangePeriodValue(periodValue: Float) {
        onDispatcher(ioDispatcher) { repository.updateChangePeriodValue(periodValue) }
    }

    fun resetSettings() {
        onDispatcher(ioDispatcher) { repository.resetAllSettings() }
    }

    fun contactSupport() {
        sharingTools.contactSupport()
    }

    fun cancelWorks(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag(WORK_AUTO_WALLPAPER)
        Timber.tag(TAG).d("cancelWorks - $WORK_AUTO_WALLPAPER")
    }

    fun saveSettings(context: Context, settings: Settings) {
        onDispatcher(ioDispatcher) {
            val workManager = WorkManager.getInstance(context)
            if (settings.autoChangeWallpaper && favorites.value.isNotEmpty()) {
                workTools.setupAllWorks(
                    favorites = favorites.value,
                    timeUnit = getTimeUnit(settings.selectedButton),
                    timeValue = settings.sliderValue
                )
                Timber.tag(TAG).d("autoChangeWallpaper - true")
            } else {
                workManager.cancelAllWorkByTag(WORK_AUTO_WALLPAPER)
                Timber.tag(TAG).d("autoChangeWallpaper - false")
            }
        }
    }

    private fun getTimeUnit(buttonId: Int): TimeUnit {
        return when (buttonId) {
            R.id.minutes_radio_button -> TimeUnit.MINUTES
            R.id.hours_radio_button -> TimeUnit.HOURS
            else -> TimeUnit.DAYS
        }
    }
}