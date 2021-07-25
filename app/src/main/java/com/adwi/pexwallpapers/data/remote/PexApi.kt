package com.adwi.pexwallpapers.data.remote

import com.adwi.pexwallpapers.BuildConfig
import com.adwi.pexwallpapers.data.remote.dto.WallpaperDtoFull
import com.adwi.pexwallpapers.data.remote.dto.WallpaperDtoMinimal
import com.adwi.pexwallpapers.data.remote.dto.WallpaperResponseMinimal
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PexApi {
    companion object {
        const val BASE_URL = "https://api.pexels.com/v1/"
        const val CURATED_PAGE_SIZE = 5
        const val STANDARD_PAGE_SIZE = 10
        const val API_KEY = BuildConfig.PEX_API_ACCESS_KEY
        const val AUTHORIZATION = "Authorization"
    }

    @GET("search")
    suspend fun getPhotosOfCategory(
        @Query("query") searchCategory: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): WallpaperResponseMinimal

    @Headers("Authorization: $API_KEY")
    @GET("curated")
    suspend fun getCuratedPhotos(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): WallpaperDtoMinimal

    @GET("search")
    suspend fun getPhotoById(
        @Query("query") photoId: Int,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 1
    ): WallpaperDtoFull
}