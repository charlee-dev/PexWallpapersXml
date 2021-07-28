package com.adwi.pexwallpapers.data.remote

import com.adwi.pexwallpapers.data.remote.dto.WallpaperResponse
import com.adwi.pexwallpapers.util.Constants.Companion.CURATED_PAGE_SIZE
import com.adwi.pexwallpapers.util.Constants.Companion.SEARCH_PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Query

interface PexApi {

    @GET("v1/search")
    suspend fun searchWallpapers(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = SEARCH_PAGE_SIZE
    ): WallpaperResponse

    @GET("v1/curated")
    suspend fun getCuratedWallpapers(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = CURATED_PAGE_SIZE
    ): WallpaperResponse
}