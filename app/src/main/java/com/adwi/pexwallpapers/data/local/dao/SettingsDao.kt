package com.adwi.pexwallpapers.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adwi.pexwallpapers.data.local.entity.ChangeWallpaperPeriod
import com.adwi.pexwallpapers.data.local.entity.Settings

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(setting: Settings)

    @Query("SELECT * FROM settings WHERE id = :id")
    fun getSettings(id: Int = 1): Settings

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

    @Query("UPDATE settings SET changeWallpaperPeriod = :period")
    suspend fun updateChangePeriodType(period: ChangeWallpaperPeriod)

    @Query("UPDATE settings SET changePeriod = :periodValue")
    suspend fun updateChangePeriodValue(periodValue: Int)
}