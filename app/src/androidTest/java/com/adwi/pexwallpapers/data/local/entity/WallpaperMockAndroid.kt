package com.adwi.pexwallpapers.data.local.entity

object WallpaperMockAndroid {

    val first = Wallpaper(
        id = 1,
        width = 3024,
        height = 3024,
        url = "https://www.pexels.com/photo/brown-rocks-during-golden-hour-2014422/",
        photographer = "Joey Farina",
        photographerUrl = "https://www.pexels.com/@joey",
        color = "white",
        imageUrl = "test.com/imageUrl",
        categoryName = "Flowers",
        src = null,
        isFavorite = false,
        updatedAt = 163066497202
    )
    val second = Wallpaper(
        id = 2,
        width = 3024,
        height = 3024,
        url = "https://www.pexels.com/photo/brown-rocks-during-golden-hour-2014425/",
        photographer = "Ad Wi",
        photographerUrl = "https://www.pexels.com/@adwi",
        color = "red",
        imageUrl = "test.com/imageUrl",
        categoryName = "Cars",
        src = null,
        isFavorite = true,
        updatedAt = 1630664958796
    )
    val third = Wallpaper(
        id = 3,
        width = 3024,
        height = 3024,
        url = "https://www.pexels.com/photo/brown-rocks-during-golden-hour-2014425/",
        photographer = "Ad Wi",
        photographerUrl = "https://www.pexels.com/@adwi",
        color = "red",
        imageUrl = "test.com/imageUrl",
        categoryName = "Cars",
        src = null,
        isFavorite = true,
        updatedAt = 1630664975234
    )
    val forth = Wallpaper(
        id = 4,
        width = 3024,
        height = 3024,
        url = "https://www.pexels.com/photo/brown-rocks-during-golden-hour-2014425/",
        photographer = "Ad Wi",
        photographerUrl = "https://www.pexels.com/@adwi",
        color = "red",
        imageUrl = "test.com/imageUrl",
        categoryName = "Flowers",
        src = null,
        isFavorite = false,
        updatedAt = 163066495879
    )

    val list = listOf(first, second, third, forth)
}