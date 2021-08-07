package com.adwi.pexwallpapers.data.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Src(
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val landscape: String,
    val portrait: String,
    val tiny: String
) : Parcelable

