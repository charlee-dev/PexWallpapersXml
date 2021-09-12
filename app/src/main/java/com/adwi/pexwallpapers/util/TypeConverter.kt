package com.adwi.pexwallpapers.util

import com.adwi.pexwallpapers.data.local.entity.CuratedWallpapers
import com.adwi.pexwallpapers.data.local.entity.SearchResult
import com.adwi.pexwallpapers.data.local.entity.Src
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.remote.dto.WallpaperDto

object TypeConverter {

    fun wallpaperDtoToWallpaper(
        wallpaper: WallpaperDto,
        categoryName: String,
        isFavorite: Boolean
    ) =
        Wallpaper(
            id = wallpaper.id,
            photographer = wallpaper.photographer,
            color = wallpaper.color,
            imageUrl = wallpaper.src.portrait,
            height = heights.random(),
            width = wallpaper.width,
            url = wallpaper.pexUrl,
            photographerUrl = wallpaper.photographerUrl,
            isFavorite = isFavorite,
            categoryName = categoryName,
            src = Src(
                original = wallpaper.src.original,
                large2x = wallpaper.src.large2x,
                large = wallpaper.src.large,
                medium = wallpaper.src.medium,
                landscape = wallpaper.src.landscape,
                portrait = wallpaper.src.portrait,
                tiny = wallpaper.src.tiny
            )
        )

    fun wallpaperToCuratedWallpaper(wallpaper: Wallpaper) =
        CuratedWallpapers(wallpaper.id)

    fun wallpaperToSearchResult(searchQuery: String, wallpaper: Wallpaper, queryPosition: Int) =
        SearchResult(
            searchQuery = searchQuery,
            wallpaperId = wallpaper.id,
            queryPosition = queryPosition
        )

    private val heights = listOf(830, 1220, 975, 513, 600, 790)
}