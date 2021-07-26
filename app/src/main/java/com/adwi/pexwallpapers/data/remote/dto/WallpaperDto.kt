package com.adwi.pexwallpapers.data.remote.dto

import com.adwi.pexwallpapers.data.local.entity.Src
import com.google.gson.annotations.SerializedName


data class WallpaperDto(
    val id: Int,
    val width: Int?,
    val height: Int?,
    @SerializedName("url")
    val pexUrl: String?,
    val photographer: String,
    @SerializedName("photographer_url")
    val photographerUrl: String?,
    @SerializedName("avg_color")
    val color: String,
    val src: Src
)