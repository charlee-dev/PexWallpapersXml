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
    var sliderMinutes: Float = 5f,
    var sliderHours: Float = 5f,
    var sliderDays: Float = 5f,
    var downloadOverWiFi: Boolean = true
)

val defaultSettings = Settings(
    id = 1,
    lastQuery = "Flowers",
    pushNotification = true,
    newWallpaperSet = false,
    wallpaperRecommendations = true,
    autoChangeWallpaper = false,
    selectedButton = R.id.hours_radio_button,
    sliderMinutes = 5f,
    sliderHours = 5f,
    sliderDays = 5f,
    downloadOverWiFi = false
)