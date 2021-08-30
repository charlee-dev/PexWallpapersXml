package com.adwi.pexwallpapers.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adwi.pexwallpapers.R


@Entity(tableName = "settings")
class Settings(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    var lastQuery: String = "Flowers",
    var pushNotification: Boolean = true,
    var newWallpaperSet: Boolean = false,
    var wallpaperRecommendations: Boolean = true,
    var autoChangeWallpaper: Boolean = false,
    var selectedButton: Int = R.id.hours_radio_button,
    var sliderValue: Float = 5f,
    var downloadOverWiFi: Boolean = true
)

val defaultSettings = Settings(
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