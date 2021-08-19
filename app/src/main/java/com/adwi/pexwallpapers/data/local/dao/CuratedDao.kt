package com.adwi.pexwallpapers.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adwi.pexwallpapers.data.local.entity.CuratedWallpapers
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import kotlinx.coroutines.flow.Flow

@Dao
interface CuratedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCuratedWallpapers(wallpapers: List<CuratedWallpapers>)

    @Query("SELECT * FROM curated_wallpapers INNER JOIN wallpaper_table ON id = wallpaperId")
    fun getAllCuratedWallpapers(): Flow<List<Wallpaper>>

    @Query("DELETE FROM curated_wallpapers")
    suspend fun deleteAllCuratedWallpapers()
}