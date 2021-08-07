package com.adwi.pexwallpapers.util

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.adwi.pexwallpapers.R
import com.github.ajalt.timberkt.d
import timber.log.Timber

object ShareUtil {
    fun shareWallpaper(activity: Activity, url: String) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name))
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage = url.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(activity, Intent.createChooser(shareIntent, "choose one"), null)
        } catch (e: Exception) {
            Timber.tag("ShareUtil").d { "ShareIntent: $e" }
        }
    }
}