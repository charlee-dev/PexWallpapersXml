package com.adwi.pexwallpapers.data.local.entity

object SearchMock {

    val first = SearchResult(
        searchQuery = "Flowers",
        wallpaperId = 1,
        queryPosition = 1
    )

    val second = SearchResult(
        searchQuery = "Cars",
        wallpaperId = 2,
        queryPosition = 1
    )

    val third = SearchResult(
        searchQuery = "Cars",
        wallpaperId = 3,
        queryPosition = 2
    )

    val forth = SearchResult(
        searchQuery = "Flowers",
        wallpaperId = 4,
        queryPosition = 2
    )

    val list = listOf(first, second, third, forth)
}