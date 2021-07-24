package com.adwi.pexwallpapers.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WallpaperDtoMinimal(
    val id: Long,
    val photographer: String,
    @SerializedName("avg_color")
    val color: String,
    val src: SrcDtoMinimal
)

data class WallpaperDtoFull(
    val id: Int,
    val width: Int?,
    val height: Int?,
    val pexUrl: String?,
    val photographer: String,
    @SerializedName("photographer_url")
    val photographerUrl: String?,
    @SerializedName("avg_color")
    val color: String,
    val src: SrcDtoFull
)