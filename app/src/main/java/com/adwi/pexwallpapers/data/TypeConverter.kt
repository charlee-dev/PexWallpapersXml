package com.adwi.pexwallpapers.data

import com.adwi.pexwallpapers.data.local.entity.Src
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.remote.dto.WallpaperDto
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d

object TypeConverter {

    private val TAG = "TypeConverter"

    fun wallpaperDtoListToEntityList(wallpaperList: List<WallpaperDto>): List<Wallpaper> {
        val list = mutableListOf<Wallpaper>()
        for (wallpaper in wallpaperList) {
            list.add(wallpaperDtoToEntityFull(wallpaper))
        }
        Timber.tag(TAG).d { "wallpaperDtoListToEntityListMinimal - List size: ${list.size}" }
        return list
    }

    private fun wallpaperDtoMinimalToEntity(wallpaper: WallpaperDto) =
        Wallpaper(
            id = wallpaper.id,
            photographer = wallpaper.photographer,
            color = wallpaper.color,
            imageUrl = wallpaper.src.medium,
        )

    private fun wallpaperDtoToEntityFull(wallpaper: WallpaperDto) =
        Wallpaper(
            id = wallpaper.id,
            photographer = wallpaper.photographer,
            color = wallpaper.color,
            imageUrl = wallpaper.src.medium,
            height = wallpaper.height,
            width = wallpaper.width,
            url = wallpaper.pexUrl,
            photographerUrl = wallpaper.photographerUrl,
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
}