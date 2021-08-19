package com.adwi.pexwallpapers.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpapersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpapers(wallpapers: List<Wallpaper>)

    @Query("SELECT * FROM wallpaper_table WHERE id = :wallpaperId")
    fun getWallpaperById(wallpaperId: Int): Flow<Wallpaper>

    @Query("SELECT * FROM wallpaper_table")
    fun getAllWallpapers(): Flow<List<Wallpaper>>
}