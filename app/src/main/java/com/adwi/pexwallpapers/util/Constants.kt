package com.adwi.pexwallpapers.util

import com.adwi.pexwallpapers.BuildConfig

class Constants {
    companion object {

        // PexApi
        const val BASE_URL = "https://api.pexels.com/"
        const val API_KEY = BuildConfig.PEX_API_ACCESS_KEY
        const val AUTHORIZATION = "Authorization"
        const val CURATED_PAGE_SIZE = 20
        const val SEARCH_PAGE_SIZE = 20

        // Pager
        const val PAGING_SIZE = 20
        const val PAGING_MAX_SIZE = 200

        // Database
        const val WALLPAPER_DATABASE = "wallpaper_database"

        // Args
        const val WALLPAPER_ID = "WALLPAPER_ID"

        // Search

        const val LAST_QUERY = "LAST_QUERY"
    }
}