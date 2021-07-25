package com.adwi.pexwallpapers.di

import android.app.Application
import androidx.room.Room
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.WallpapersDao
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
                "wallpaper_database"
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providePhotoDao(appDatabase: WallpaperDatabase): WallpapersDao =
        appDatabase.wallpaperDao()
}