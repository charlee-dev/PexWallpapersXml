package com.adwi.pexwallpapers.data.remote.dto

data class SrcDtoFull(
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val landscape: String,
    val portrait: String,
    val tiny: String
)

data class SrcDtoMinimal(
    val medium: String
)