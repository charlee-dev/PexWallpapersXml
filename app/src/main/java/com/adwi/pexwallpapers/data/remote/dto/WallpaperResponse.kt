package com.adwi.pexwallpapers.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WallpaperResponseFull(
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("photos")
    val wallpaperList: List<WallpaperDtoFull>,
    @SerializedName("next_page")
    val nextPage: String
)

data class WallpaperResponseMinimal(
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("photos")
    val wallpaperList: List<WallpaperDtoFull>,
    @SerializedName("next_page")
    val nextPage: String
)
