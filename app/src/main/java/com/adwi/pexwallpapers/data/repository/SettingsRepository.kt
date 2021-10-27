package com.adwi.pexwallpapers.data.repository

import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.entity.Settings
import com.adwi.pexwallpapers.data.repository.interfaces.SettingsRepositoryInterface
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val wallpapersDatabase: WallpaperDatabase
) : SettingsRepositoryInterface {

    private val dao = wallpapersDatabase.settingsDao()

    override fun getSettings() = dao.getSettings()

    override suspend fun insertSettings(settings: Settings) =
        dao.insertSettings(settings)

    override suspend fun updateLastQuery(query: String) =
        dao.updateLastQuery(query)

    override suspend fun updateNewWallpaperSet(enabled: Boolean) =
        dao.updateNewWallpaperSet(enabled)

    override suspend fun updateWallpaperRecommendations(enabled: Boolean) =
        dao.updateWallpaperRecommendations(enabled)

    override suspend fun updateAutoChangeWallpaper(enabled: Boolean) =
        dao.updateAutoChangeWallpaper(enabled)

    override suspend fun updateDownloadOverWiFi(enabled: Boolean) =
        dao.updateDownloadOverWiFi(enabled)

    override suspend fun updateChangePeriodType(radioButton: Int) =
        dao.updateChangePeriodType(radioButton)

    override suspend fun updateChangePeriodValue(periodValue: Float) =
        dao.updateChangePeriodValue(periodValue)

    override suspend fun resetAllSettings() = dao.insertSettings(Settings())
}