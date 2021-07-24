package com.adwi.pexwallpapers.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adwi.pexwallpapers.data.local.entity.Src


@Entity(tableName = "wallpaper_table")
data class Wallpaper(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    val photographerUrl: String,
    val avgColor: String,
    @Embedded
    val src: Src,
    val isFavorite: Boolean = false
)
