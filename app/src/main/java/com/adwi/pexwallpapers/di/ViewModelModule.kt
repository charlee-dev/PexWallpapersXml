package com.adwi.pexwallpapers.di

import android.content.Context
import com.adwi.pexwallpapers.data.local.WallpaperDatabase
import com.adwi.pexwallpapers.data.remote.PexApi
import com.adwi.pexwallpapers.data.repository.*
import com.adwi.pexwallpapers.data.repository.interfaces.*
import com.adwi.pexwallpapers.shared.tools.ImageTools
import com.adwi.pexwallpapers.shared.tools.NotificationTools
import com.adwi.pexwallpapers.shared.tools.SharingTools
import com.adwi.pexwallpapers.shared.tools.WallpaperSetter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideImageTools(@ApplicationContext context: Context) = ImageTools(context)

    @Provides
    @ViewModelScoped
    fun provideNotificationTools(
        @ApplicationContext context: Context,
        imageTools: ImageTools
    ) = NotificationTools(context, imageTools)

    @Provides
    @ViewModelScoped
    fun provideWallpaperSetter(
        @ApplicationContext context: Context,
        imageTools: ImageTools
    ) = WallpaperSetter(context, imageTools)

    @Provides
    @ViewModelScoped
    fun provideSharingTools(
        @ApplicationContext context: Context,
        imageTools: ImageTools
    ) = SharingTools(context, imageTools)

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
    fun provideSuggestionsRepository(
        wallpapersDatabase: WallpaperDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) = SuggestionsRepository(wallpapersDatabase, ioDispatcher) as SuggestionsRepositoryInterface

    @Provides
    @ViewModelScoped
    fun provideSettingsRepository(
        wallpapersDatabase: WallpaperDatabase
    ) = SettingsRepository(wallpapersDatabase) as SettingsRepositoryInterface
}