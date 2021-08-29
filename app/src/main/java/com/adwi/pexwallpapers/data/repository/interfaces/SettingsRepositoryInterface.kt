package com.adwi.pexwallpapers.data.repository.interfaces

import com.adwi.pexwallpapers.data.local.dao.SettingsDao
import com.adwi.pexwallpapers.data.local.entity.Settings

interface SettingsRepositoryInterface {

    val dao: SettingsDao

    suspend fun getSettings(): Settings
    suspend fun insertSettings(settings: Settings)
    suspend fun updateLastQuery(query: String)
    suspend fun updatePushNotification(enabled: Boolean)
    suspend fun updateNewWallpaperSet(enabled: Boolean)
    suspend fun updateWallpaperRecommendations(enabled: Boolean)
    suspend fun updateAutoChangeWallpaper(enabled: Boolean)
    suspend fun updateDownloadOverWiFi(enabled: Boolean)
    suspend fun updateChangePeriodType(radioButton: Int)
    suspend fun updateChangePeriodValue(periodValue: Float)
    suspend fun resetAllSettings()
}