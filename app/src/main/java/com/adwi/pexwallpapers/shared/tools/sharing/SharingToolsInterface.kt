package com.adwi.pexwallpapers.shared.tools.sharing

import android.content.Context

interface SharingToolsInterface {

    fun openUrlInBrowser(url: String)

    fun contactSupport()

    suspend fun shareImage(context: Context, imageUrl: String, photographer: String)
}