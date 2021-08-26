package com.adwi.pexwallpapers.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adwi.pexwallpapers.data.local.dao.*
import com.adwi.pexwallpapers.data.local.entity.*

@Database(
    entities = [
        Wallpaper::class,
        CuratedWallpapers::class,
        SearchResult::class,
        SearchQueryRemoteKey::class,
        Suggestion::class,
        Settings::class
    ],
    version = 2
)
abstract class WallpaperDatabase : RoomDatabase() {

    abstract fun curatedDao(): CuratedDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun searchDao(): SearchDao
    abstract fun searchQueryRemoteKeyDao(): SearchQueryRemoteKeyDao
    abstract fun suggestionsDao(): SuggestionsDao
    abstract fun wallpaperDao(): WallpapersDao
    abstract fun settingsDao(): SettingsDao
}