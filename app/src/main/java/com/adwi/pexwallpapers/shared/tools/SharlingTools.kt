package com.adwi.pexwallpapers.shared.tools

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class SharingTools @Inject constructor(
    @ActivityContext private val context: Context,
    private val imageTools: ImageTools
) {

    fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.parse(url)
        }
        startActivity(context, intent, null)
    }

    fun contactSupport() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.parse("mailto:adrianwitaszak@gmial.com")
            putExtra(Intent.EXTRA_SUBJECT, "Support request Nr. 12345678")
        }
        startActivity(context, Intent.createChooser(intent, "Choose an app"), null)
    }

    suspend fun shareImage(imageUrl: String, photographer: String) {
        val uri = imageTools.saveImageToInternalStorage(imageUrl)
        val intent = Intent(Intent.ACTION_SEND).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, "image/*")
            putExtra(Intent.EXTRA_SUBJECT, "Picture by $photographer")
            putExtra(Intent.EXTRA_TITLE, "Picture by PexWallpapers")
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        // TODO() sort out startActivity FLAG_ACTIVITY_NEW_TASK not working
        startActivity(
            context,
            intent,
//            Intent.createChooser(intent, "Choose an app"),
            null
        )
    }
}