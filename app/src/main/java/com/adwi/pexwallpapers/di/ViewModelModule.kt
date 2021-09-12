package com.adwi.pexwallpapers.di

import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.remote.PexApi
import com.adwi.pexwallpapers.data.repository.FavoritesRepository
import com.adwi.pexwallpapers.data.repository.SearchRepository
import com.adwi.pexwallpapers.data.repository.SettingsRepository
import com.adwi.pexwallpapers.data.repository.WallpaperRepository
import com.adwi.pexwallpapers.data.repository.interfaces.FavoritesRepositoryInterface
import com.adwi.pexwallpapers.data.repository.interfaces.SearchRepositoryInterface
import com.adwi.pexwallpapers.data.repository.interfaces.SettingsRepositoryInterface
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideWallpaperRepository(
        pexApi: PexApi,
        wallpapersDatabase: WallpaperDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) =
        WallpaperRepository(
            pexApi,
            wallpapersDatabase,
            ioDispatcher
        ) as WallpaperRepositoryInterface

    @Provides
    @ViewModelScoped
    fun provideSearchRepository(
        pexApi: PexApi,
        wallpapersDatabase: WallpaperDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) = SearchRepository(pexApi, wallpapersDatabase, ioDispatcher) as SearchRepositoryInterface

    @Provides
    @ViewModelScoped
    fun provideFavoritesRepository(
        wallpapersDatabase: WallpaperDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) = FavoritesRepository(wallpapersDatabase, ioDispatcher) as FavoritesRepositoryInterface

    @Provides
    @ViewModelScoped
    fun provideSettingsRepository(
        wallpapersDatabase: WallpaperDatabase
    ) = SettingsRepository(wallpapersDatabase) as SettingsRepositoryInterface
}