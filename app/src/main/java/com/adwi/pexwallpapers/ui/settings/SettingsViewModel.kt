package com.adwi.pexwallpapers.ui.settings

import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Settings
import com.adwi.pexwallpapers.data.repository.SettingsRepository
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import com.adwi.pexwallpapers.util.onIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: SettingsRepository) :
    BaseViewModel() {

    val currentChangePeriodType =
        MutableStateFlow(R.id.hours_radio_button)

    private val _currentSettings = MutableStateFlow(Settings())
    val currentSettings: StateFlow<Settings> = _currentSettings

    init {
        onIO {
            _currentSettings.value = repository.getSettings()
        }
    }

    fun updatePushNotification(enabled: Boolean) {
        onIO { repository.updatePushNotification(enabled) }
    }

    fun updateNewWallpaperSet(enabled: Boolean) {
        onIO { repository.updateNewWallpaperSet(enabled) }
    }

    fun updateWallpaperRecommendations(enabled: Boolean) {
        onIO { repository.updateWallpaperRecommendations(enabled) }
    }

    fun updateAutoChangeWallpaper(enabled: Boolean) {
        onIO { repository.updateAutoChangeWallpaper(enabled) }
    }

    fun updateDownloadOverWiFi(enabled: Boolean) {
        onIO { repository.updateDownloadOverWiFi(enabled) }
    }

    fun updateChangePeriodType(radioButton: Int) {
        currentChangePeriodType.value = radioButton
        onIO { repository.updateChangePeriodType(radioButton) }
    }

    fun updateChangePeriodValue(periodValue: Float) {
        onIO { repository.updateChangePeriodValue(periodValue) }
    }

    fun resetSettings() {
        onIO { repository.resetAllSettings() }
    }
}