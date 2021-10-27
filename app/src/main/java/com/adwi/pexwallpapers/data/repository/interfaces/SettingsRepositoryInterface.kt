package com.adwi.pexwallpapers.data.repository.interfaces

import com.adwi.pexwallpapers.data.local.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepositoryInterface {

    fun getSettings(): Flow<Settings>
    suspend fun insertSettings(settings: Settings)
    suspend fun updateLastQuery(query: String)
    suspend fun updateNewWallpaperSet(enabled: Boolean)
    suspend fun updateWallpaperRecommendations(enabled: Boolean)
    suspend fun updateAutoChangeWallpaper(enabled: Boolean)
    suspend fun updateDownloadOverWiFi(enabled: Boolean)
    suspend fun updateChangePeriodType(radioButton: Int)
    suspend fun updateChangePeriodValue(periodValue: Float)
    suspend fun resetAllSettings()
}