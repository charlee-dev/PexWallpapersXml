package com.adwi.pexwallpapers.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
class Settings(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val lastQuery: String = "",
    var pushNotification: Boolean = true,
    var newWallpaperSet: Boolean = true,
    var wallpaperRecommendations: Boolean = true,
    var autoChangeWallpaper: Boolean = true,
    var changeWallpaperPeriod: ChangeWallpaperPeriod = ChangeWallpaperPeriod.MINUTES,
    val changePeriod: Int = 5,
    var downloadOverWiFi: Boolean = true
)

enum class ChangeWallpaperPeriod { MINUTES, HOURS, DAYS, WEEKS }