package com.adwi.pexwallpapers.di

import com.adwi.pexwallpapers.data.WallpaperRepository
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.remote.PexApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideWallpaperRepository(
        pexApi: PexApi,
        wallpapersDatabase: WallpaperDatabase
    ): WallpaperRepository =
        WallpaperRepository(pexApi, wallpapersDatabase)
}