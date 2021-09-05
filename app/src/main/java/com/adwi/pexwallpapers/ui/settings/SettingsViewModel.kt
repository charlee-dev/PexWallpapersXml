package com.adwi.pexwallpapers.ui.settings

import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Settings
import com.adwi.pexwallpapers.data.local.entity.defaultSettings
import com.adwi.pexwallpapers.data.repository.interfaces.SettingsRepositoryInterface
import com.adwi.pexwallpapers.di.IoDispatcher
import com.adwi.pexwallpapers.shared.tools.NotificationTools
import com.adwi.pexwallpapers.shared.tools.SharingTools
import com.adwi.pexwallpapers.ui.base.BaseViewModel
import com.adwi.pexwallpapers.util.onDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepositoryInterface,
    private val sharingTools: SharingTools,
    private val notificationTools: NotificationTools,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    val currentChangePeriodType =
        MutableStateFlow(R.id.hours_radio_button)

    private val _currentSettings = MutableStateFlow(defaultSettings)
    val currentSettings: StateFlow<Settings> = _currentSettings

    init {
        onDispatcher(ioDispatcher) {
            _currentSettings.value = repository.getSettings()
        }
    }


    fun updatePushNotification(enabled: Boolean) {
        onDispatcher(ioDispatcher) {
            repository.updatePushNotification(enabled)
            if (enabled) {
                notificationTools.createNotificationChannel()
                notificationTools.sendNotification()
            }
        }
    }

    fun updateNewWallpaperSet(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateNewWallpaperSet(enabled) }
    }

    fun updateWallpaperRecommendations(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateWallpaperRecommendations(enabled) }
    }

    fun updateAutoChangeWallpaper(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateAutoChangeWallpaper(enabled) }
    }

    fun updateDownloadOverWiFi(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateDownloadOverWiFi(enabled) }
    }

    fun updateChangePeriodType(radioButton: Int) {
        currentChangePeriodType.value = radioButton
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
}