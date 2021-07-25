//package com.adwi.pexwallpapers.di
//
//import com.adwi.pexwallpapers.data.local.WallpapersDao
//import com.adwi.pexwallpapers.data.remote.PexApi
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ViewModelComponent
//import dagger.hilt.android.scopes.ViewModelScoped
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//
//@Module
//@InstallIn(ViewModelComponent::class)
//object RepositoryModule {
//
//    @Provides
//    @ViewModelScoped
//    fun provideWallpaperRepository(
//        pexApi: PexApi,
//        wallpapersDao: WallpapersDao
//    ): WallpaperRepository =
//        WallpaperRepository(pexApi, wallpapersDao)
//
//    @Provides
//    @ViewModelScoped
//    fun provideSearchRepository(
//        pexApi: PexApi,
//        wallpapersDao: WallpapersDao
//    ): SearchRepository =
//        SearchRepository(pexApi, wallpapersDao)
//
//    @Provides
//    @ViewModelScoped
//    fun providePreviewRepository(wallpapersDao: WallpapersDao): PreviewRepository =
//        PreviewRepository(wallpapersDao)
//}