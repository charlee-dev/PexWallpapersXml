package com.adwi.pexwallpapers.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.util.Constants.Companion.WALLPAPER_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room
            .databaseBuilder(
                context,
                WallpaperDatabase::class.java,
                WALLPAPER_DATABASE
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideWallpaperDao(appDatabase: WallpaperDatabase) =
        appDatabase.wallpaperDao()

    @Provides
    @Singleton
    fun provideFavoritesDao(appDatabase: WallpaperDatabase) =
        appDatabase.favoritesDao()

    @Provides
    @Singleton
    fun provideSearchDao(appDatabase: WallpaperDatabase) =
        appDatabase.searchDao()

    @Provides
    @Singleton
    fun provideSuggestionsDao(appDatabase: WallpaperDatabase) =
        appDatabase.suggestionsDao()

    @Provides
    @Singleton
    fun provideCuratedDao(appDatabase: WallpaperDatabase) =
        appDatabase.curatedDao()

    @Provides
    @Singleton
    fun provideSettingsDao(appDatabase: WallpaperDatabase) =
        appDatabase.settingsDao()
}