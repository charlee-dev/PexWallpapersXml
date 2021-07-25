package com.adwi.pexwallpapers.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adwi.pexwallpapers.data.local.entity.Wallpaper

@Database(entities = [Wallpaper::class], version = 1)
abstract class WallpaperDatabase: RoomDatabase() {
    abstract fun wallpaperDao(): WallpapersDao
}