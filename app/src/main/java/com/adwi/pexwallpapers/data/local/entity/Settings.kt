package com.adwi.pexwallpapers.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adwi.pexwallpapers.R


@Entity(tableName = "settings")
class Settings(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val lastQuery: String = "Flowers",
    val newWallpaperSet: Boolean = true,
    val wallpaperRecommendations: Boolean = true,
    val autoChangeWallpaper: Boolean = false,
    val selectedButton: Int = R.id.hours_radio_button,
    val sliderValue: Float = 5f,
    val downloadOverWiFi: Boolean = false,
    val autoHome: Boolean = true,
    val autoLock: Boolean = false
)