package com.adwi.pexwallpapers.di

import android.app.Application
import androidx.room.Room
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.dao.*
import com.adwi.pexwallpapers.util.Constants.Companion.WALLPAPER_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): WallpaperDatabase =
        Room
            .databaseBuilder(
                application,
                WallpaperDatabase::class.java,
                WALLPAPER_DATABASE
            )
//                TODO("specify autoMigrations")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideWallpaperDao(appDatabase: WallpaperDatabase): WallpapersDao =
        appDatabase.wallpaperDao()

    @Provides
    @Singleton
    fun provideFavoritesDao(appDatabase: WallpaperDatabase): FavoritesDao =
        appDatabase.favoritesDao()

    @Provides
    @Singleton
    fun provideSearchDao(appDatabase: WallpaperDatabase): SearchDao =
        appDatabase.searchDao()

    @Provides
    @Singleton
    fun provideSuggestionsDao(appDatabase: WallpaperDatabase): SuggestionsDao =
        appDatabase.suggestionsDao()

    @Provides
    @Singleton
    fun provideCuratedDao(appDatabase: WallpaperDatabase): CuratedDao =
        appDatabase.curatedDao()

    @Provides
    @Singleton
    fun provideSettingsDao(appDatabase: WallpaperDatabase): SettingsDao =
        appDatabase.settingsDao()
}