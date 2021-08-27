package com.adwi.pexwallpapers.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adwi.pexwallpapers.data.local.entity.Settings

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(setting: Settings)

    @Query("SELECT * FROM settings WHERE id = 1")
    suspend fun getSettings(): Settings

    @Query("UPDATE settings SET lastQuery = :query")
    suspend fun updateLastQuery(query: String)

    @Query("UPDATE settings SET pushNotification = :enabled")
    suspend fun updatePushNotification(enabled: Boolean)

    @Query("UPDATE settings SET newWallpaperSet = :enabled")
    suspend fun updateNewWallpaperSet(enabled: Boolean)

    @Query("UPDATE settings SET wallpaperRecommendations = :enabled")
    suspend fun updateWallpaperRecommendations(enabled: Boolean)

    @Query("UPDATE settings SET autoChangeWallpaper = :enabled")
    suspend fun updateAutoChangeWallpaper(enabled: Boolean)

    @Query("UPDATE settings SET downloadOverWiFi = :enabled")
    suspend fun updateDownloadOverWiFi(enabled: Boolean)

    @Query("UPDATE settings SET selectedButton = :radioButton")
    suspend fun updateChangePeriodType(radioButton: Int)

    @Query("UPDATE settings SET sliderValue = :periodValue")
    suspend fun updateChangePeriodValue(periodValue: Float)
}