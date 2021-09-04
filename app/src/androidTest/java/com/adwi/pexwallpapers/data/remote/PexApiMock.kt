package com.adwi.pexwallpapers.data.remote

import com.adwi.pexwallpapers.data.remote.dto.WallpaperResponse

class PexApiMock : PexApi {

    override suspend fun searchWallpapers(
        query: String,
        page: Int,
        perPage: Int
    ): WallpaperResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getCuratedWallpapers(page: Int, perPage: Int): WallpaperResponse {
        TODO("Not yet implemented")
    }
}