package com.adwi.pexwallpapers.di

import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.remote.PexApi
import com.adwi.pexwallpapers.data.repository.PreviewRepository
import com.adwi.pexwallpapers.data.repository.SearchRepository
import com.adwi.pexwallpapers.data.repository.WallpaperRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

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
    fun providePreviewRepository(
        pexApi: PexApi,
        wallpapersDatabase: WallpaperDatabase
    ): PreviewRepository =
        PreviewRepository(pexApi, wallpapersDatabase)
}