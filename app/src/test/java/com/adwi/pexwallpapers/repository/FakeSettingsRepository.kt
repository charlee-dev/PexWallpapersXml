package com.adwi.pexwallpapers.repository

import com.adwi.pexwallpapers.data.local.entity.Settings
import com.adwi.pexwallpapers.data.local.entity.defaultSettings
import com.adwi.pexwallpapers.data.repository.interfaces.SettingsRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

class FakeSettingsRepository : SettingsRepositoryInterface {

    private var settingsItem = Settings()
    private val settingsFlow = MutableStateFlow(settingsItem)

    override suspend fun getSettings(): Settings {
        return settingsFlow.first()
    }

    override suspend fun insertSettings(settings: Settings) {
        settingsItem = settings
    }

    override suspend fun updateLastQuery(query: String) {
        settingsItem.lastQuery = query
    }

    override suspend fun updatePushNotification(enabled: Boolean) {
        settingsItem.pushNotification = enabled
    }

    override suspend fun updateNewWallpaperSet(enabled: Boolean) {
        settingsItem.newWallpaperSet = enabled
    }

    override suspend fun updateWallpaperRecommendations(enabled: Boolean) {
        settingsItem.wallpaperRecommendations = enabled
    }

    override suspend fun updateAutoChangeWallpaper(enabled: Boolean) {
        settingsItem.autoChangeWallpaper = enabled
    }

    override suspend fun updateDownloadOverWiFi(enabled: Boolean) {
        settingsItem.downloadOverWiFi = enabled
    }

    override suspend fun updateChangePeriodType(radioButton: Int) {
        settingsItem.selectedButton = radioButton
    }

    override suspend fun updateChangePeriodValue(periodValue: Float) {
        settingsItem.sliderValue = periodValue
    }

    override suspend fun resetAllSettings() {
        settingsItem = defaultSettings
    }

    private fun refreshFlow() {
        settingsFlow.value = settingsItem
    }
}