package com.adwi.pexwallpapers.data.local

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaper(wallpaper: Wallpaper)

    @Query("SELECT * FROM wallpaper_table")
    fun getAllWallpapers(): Flow<List<Wallpaper>>

    @Query("DELETE FROM wallpaper_table")
    suspend fun deleteAllWallpapers()
}