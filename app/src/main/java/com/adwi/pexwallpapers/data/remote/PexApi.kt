package com.adwi.pexwallpapers.data.remote

import com.adwi.pexwallpapers.data.remote.dto.WallpaperResponse
import com.adwi.pexwallpapers.util.Constants.Companion.CURATED_PAGE_SIZE
import com.adwi.pexwallpapers.util.Constants.Companion.STANDARD_PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Query

interface PexApi {

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