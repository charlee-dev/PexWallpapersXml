package com.adwi.pexwallpapers.mock

import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Settings

object SettingsMock {

    val settings = Settings(
        id = 1,
        lastQuery = "Flowers",
        pushNotification = false,
        newWallpaperSet = false,
        wallpaperRecommendations = true,
        autoChangeWallpaper = false,
        selectedButton = R.id.hours_radio_button,
        sliderValue = 5f,
        downloadOverWiFi = false
    )
}