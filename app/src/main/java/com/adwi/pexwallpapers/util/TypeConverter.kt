package com.adwi.pexwallpapers.util

import com.adwi.pexwallpapers.data.local.entity.CuratedWallpapers
import com.adwi.pexwallpapers.data.local.entity.SearchResult
import com.adwi.pexwallpapers.data.local.entity.Src
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.remote.dto.WallpaperDto

object TypeConverter {

    fun wallpaperDtoToWallpaper(wallpaper: WallpaperDto, isFavorite: Boolean) =
        Wallpaper(
            id = wallpaper.id,
            photographer = wallpaper.photographer,
            color = wallpaper.color,
            imageUrl = wallpaper.src.portrait,
            height = wallpaper.height,
            width = wallpaper.width,
            url = wallpaper.pexUrl,
            photographerUrl = wallpaper.photographerUrl,
            isFavorite = isFavorite,
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
}