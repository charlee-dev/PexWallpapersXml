package com.adwi.pexwallpapers.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "wallpaper_table")
data class Wallpaper(
    @PrimaryKey val id: Int,
    val width: Int? = null,
    val height: Int? = null,
    val url: String? = null,
    val photographer: String,
    val photographerUrl: String? = null,
    val color: String,
    val imageUrl: String,
    @Embedded val src: Src? = null,
    val isFavorite: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "curated_wallpapers")
data class CuratedWallpapers(
    @PrimaryKey val wallpaperId: Int
)

@Entity(tableName = "search_results", primaryKeys = ["searchQuery", "wallpaperId"])
data class SearchResult(
    val searchQuery: String,
    val wallpaperId: Int,
    val queryPosition: Int
)
