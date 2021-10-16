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
import com.adwi.pexwallpapers.shared.work.TAG_NEW_WALLPAPER
import com.adwi.pexwallpapers.shared.work.WorkTools
import com.adwi.pexwallpapers.ui.base.BaseViewModel
import com.adwi.pexwallpapers.util.onDispatcher
import timber.log.Timber
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepositoryInterface,
    private val favoritesRepository: FavoritesRepository,
    private val sharingTools: SharingTools,
    private val workTools: WorkTools,
    private val notificationTools: NotificationTools,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext context: Context
) : BaseViewModel() {

    val settings = repository.getSettings()

    private val favorites = MutableStateFlow<List<Wallpaper>>(emptyList())

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
        Timber.d("push: $checked")
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

    private fun getTimeUnit(buttonId: Int): TimeUnit {
        return when (buttonId) {
            R.id.minutes_radio_button -> TimeUnit.MINUTES
            R.id.hours_radio_button -> TimeUnit.HOURS
            else -> TimeUnit.DAYS
        }
    }

    fun setupWorks(context: Context, settings: Settings) {
        onDispatcher(ioDispatcher) {
            val workManager = WorkManager.getInstance(context)
            if (settings.autoChangeWallpaper && favorites.value.isNotEmpty()) {
                workTools.setupAllWorks(
                    favorites = favorites.value,
                    timeUnit = getTimeUnit(settings.selectedButton),
                    timeValue = settings.sliderMinutes
                )
                Timber.tag(TAG).d("autoChangeWallpaper - true")
            } else {
                workManager.cancelAllWorkByTag(TAG_NEW_WALLPAPER)
                Timber.tag(TAG).d("autoChangeWallpaper - false")
            }
        }
    }
}