package com.adwi.pexwallpapers.ui.settings

import com.adwi.pexwallpapers.data.WallpaperRepository
import com.adwi.pexwallpapers.data.local.entity.ChangeWallpaperPeriod
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import com.adwi.pexwallpapers.util.onIO

class SettingsViewModel(private val repository: WallpaperRepository) : BaseViewModel() {

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

    fun updateChangeWallpaperPeriod(period: ChangeWallpaperPeriod) {
        onIO { repository.updateChangePeriodType(period) }
    }

    fun updateChangePeriodValue(periodValue: Int) {
        onIO { repository.updateChangePeriodValue(periodValue) }
    }
}