package com.adwi.pexwallpapers.di

import android.app.WallpaperManager
import android.content.Context
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import com.adwi.pexwallpapers.shared.tools.notification.NotificationTools
import com.adwi.pexwallpapers.shared.tools.permissions.PermissionTools
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

    @Singleton
    @Provides
    fun providePermissionTools(
        @ApplicationContext context: Context
    ) = PermissionTools(context)

    @Singleton
    @Provides
    fun provideImageTools(
        @ApplicationContext context: Context,
        permissionTools: PermissionTools
    ) = ImageTools(context, permissionTools)

    @Singleton
    @Provides
    fun provideNotificationTools(
        @ApplicationContext context: Context,
        imageTools: ImageTools,
        permissionTools: PermissionTools
    ) = NotificationTools(context, imageTools, permissionTools)

    @Singleton
    @Provides
    fun provideWallpaperSetter(
        @ApplicationContext context: Context,
        wallpaperManager: WallpaperManager,
        permissionTools: PermissionTools
    ) = WallpaperSetter(context, wallpaperManager, permissionTools)

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