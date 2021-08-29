package com.adwi.pexwallpapers.mock

import com.adwi.pexwallpapers.data.local.entity.Wallpaper

object WallpapersMock {

    val first = Wallpaper(
        id = 1,
        width = 1920,
        height = 1080,
        url = "test.com/url",
        photographer = "Ad Wi",
        photographerUrl = "test.com/photographerUrl",
        color = "white",
        imageUrl = "test.com/imageUrl",
        categoryName = "Flowers",
        src = null,
        isFavorite = false,
        updatedAt = 1234567890
    )

    val second = Wallpaper(
        id = 2,
        width = 1024,
        height = 780,
        url = "test.com/url",
        photographer = "John Smith",
        photographerUrl = "test.com/photographerUrl",
        color = "purple",
        imageUrl = "test.com/imageUrl",
        categoryName = "Cars",
        src = null,
        isFavorite = false,
        updatedAt = 5678901234
    )

    val third = Wallpaper(
        id = 3,
        width = 9210,
        height = 8100,
        url = "test.com/url",
        photographer = "Peter Parker",
        photographerUrl = "test.com/photographerUrl",
        color = "blue",
        imageUrl = "test.com/imageUrl",
        categoryName = "Flowers",
        src = null,
        isFavorite = false,
        updatedAt = 1289034567
    )

    val wallpaperList = listOf(first, second, third)
}