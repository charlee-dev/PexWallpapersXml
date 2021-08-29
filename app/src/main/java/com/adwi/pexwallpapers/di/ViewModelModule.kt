package com.adwi.pexwallpapers.di

import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.remote.PexApi
import com.adwi.pexwallpapers.data.repository.*
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

    @Provides
    @ViewModelScoped
    fun provideSearchRepository(
        pexApi: PexApi,
        wallpapersDatabase: WallpaperDatabase
    ): SearchRepository =
        SearchRepository(pexApi, wallpapersDatabase)

    @Provides
    @ViewModelScoped
    fun provideFavoritesRepository(
        wallpapersDatabase: WallpaperDatabase
    ): FavoritesRepository =
        FavoritesRepository(wallpapersDatabase)

    @Provides
    @ViewModelScoped
    fun provideSuggestionsRepository(
        wallpapersDatabase: WallpaperDatabase
    ): SuggestionsRepository =
        SuggestionsRepository(wallpapersDatabase)

    @Provides
    @ViewModelScoped
    fun provideSettingsRepository(
        wallpapersDatabase: WallpaperDatabase
    ): SettingsRepository =
        SettingsRepository(wallpapersDatabase)
}