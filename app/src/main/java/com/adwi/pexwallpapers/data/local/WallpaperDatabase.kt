package com.adwi.pexwallpapers.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adwi.pexwallpapers.data.local.entity.CuratedWallpapers
import com.adwi.pexwallpapers.data.local.entity.SearchQueryRemoteKey
import com.adwi.pexwallpapers.data.local.entity.SearchResult
import com.adwi.pexwallpapers.data.local.entity.Wallpaper

@Database(
    entities = [
        Wallpaper::class,
        CuratedWallpapers::class,
        SearchResult::class,
        SearchQueryRemoteKey::class
    ], version = 1
)
abstract class WallpaperDatabase : RoomDatabase() {

    abstract fun wallpaperDao(): WallpapersDao

    abstract fun searchQueryRemoteKeyDao(): SearchQueryRemoteKeyDao
}