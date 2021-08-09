package com.adwi.pexwallpapers.di

import android.app.Application
import androidx.room.Room
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.local.WallpapersDao
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
    fun providePhotoDao(appDatabase: WallpaperDatabase): WallpapersDao =
        appDatabase.wallpaperDao()
}