package com.adwi.pexwallpapers.data.remote

import com.adwi.pexwallpapers.BuildConfig
import com.adwi.pexwallpapers.data.remote.dto.WallpaperResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PexApi {
    companion object {
        const val BASE_URL = "https://api.pexels.com/"
        const val CURATED_PAGE_SIZE = 5
        const val STANDARD_PAGE_SIZE = 10
        const val API_KEY = BuildConfig.PEX_API_ACCESS_KEY
        const val AUTHORIZATION = "Authorization"
    }

    @GET("search")
    suspend fun getPhotosOfCategory(
        @Query("query") searchCategory: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = STANDARD_PAGE_SIZE
    ): WallpaperResponse

    @GET("v1/curated")
    suspend fun getCuratedPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = CURATED_PAGE_SIZE
    ): WallpaperResponse

    @GET("search")
    suspend fun getPhotoById(
        @Query("query") photoId: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 1
    ): WallpaperResponse
}