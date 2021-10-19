package com.adwi.pexwallpapers.data.local.dao

import androidx.room.*
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpapersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpapers(wallpapers: List<Wallpaper>)

    @Query("SELECT * FROM wallpaper_table")
    fun getAllWallpapers(): Flow<List<Wallpaper>>

    @Query("SELECT * FROM wallpaper_table WHERE id = :wallpaperId")
    suspend fun getWallpaperById(wallpaperId: Int): Wallpaper

    @Query("SELECT * FROM wallpaper_table WHERE categoryName = :categoryName")
    fun getWallpapersOfCategory(categoryName: String): Flow<List<Wallpaper>>

    @Update
    suspend fun updateWallpaperFavorite(wallpaper: Wallpaper)
}