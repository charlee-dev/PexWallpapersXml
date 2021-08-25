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

    @Query("SELECT * FROM settings WHERE id = :id")
    fun getSettings(id: Int = 1): Settings

    @Query("UPDATE settings SET lastQuery = :query")
    suspend fun updateLastQuery(query: String)
}