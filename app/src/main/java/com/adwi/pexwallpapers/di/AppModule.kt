package com.adwi.pexwallpapers.di

import android.app.WallpaperManager
import android.content.Context
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import com.adwi.pexwallpapers.shared.tools.notification.NotificationTools
import com.adwi.pexwallpapers.shared.tools.sharing.SharingTools
import com.adwi.pexwallpapers.shared.tools.wallpaper.WallpaperSetter
import com.adwi.pexwallpapers.shared.work.WorkTools
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

//    @Singleton
//    @Provides
//    fun providePermissionTools(
//         @ActivityContext activity: Context
//    ) = PermissionTools(activity)

    @Singleton
    @Provides
    fun provideImageTools(
        @ApplicationContext context: Context
    ) = ImageTools(context)

    @Singleton
    @Provides
    fun provideNotificationTools(
        @ApplicationContext context: Context,
        imageTools: ImageTools
    ) = NotificationTools(context, imageTools)

    @Singleton
    @Provides
    fun provideWallpaperSetter(
        @ApplicationContext context: Context,
        wallpaperManager: WallpaperManager
    ) = WallpaperSetter(context, wallpaperManager)

    @Singleton
    @Provides
    fun provideSharingTools(
        @ApplicationContext context: Context,
        imageTools: ImageTools
    ) = SharingTools(context, imageTools)

    @Singleton
    @Provides
    fun provideWorkTools(
        @ApplicationContext context: Context
    ) = WorkTools(context)

    @Singleton
    @Provides
    fun provideWallpaperManager(
        @ApplicationContext context: Context
    ): WallpaperManager = WallpaperManager.getInstance(context)
}