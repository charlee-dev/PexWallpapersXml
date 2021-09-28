package com.adwi.pexwallpapers.shared.tools.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharingTools @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageTools: ImageTools
) : SharingToolsInterface {

    override fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.parse(url)
        }
        startActivity(context, intent, null)
    }

    override fun contactSupport() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.parse("mailto:adrianwitaszak@gmial.com")
            putExtra(Intent.EXTRA_SUBJECT, "Support request Nr. 12345678")
        }
        startActivity(context, Intent.createChooser(intent, "Choose an app"), null)
    }

    override suspend fun shareImage(context: Context, imageUrl: String, photographer: String) {
        val uri = imageTools.saveImageToInternalStorage(imageUrl)
        val intent = Intent(Intent.ACTION_SEND).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, "image/*")
            putExtra(Intent.EXTRA_SUBJECT, "Picture by $photographer")
            putExtra(Intent.EXTRA_TITLE, "Picture by PexWallpapers")
            putExtra(Intent.EXTRA_STREAM, uri)
        }

        startActivity(
            context,
            Intent.createChooser(intent, "Choose an app"),
            null
        )
    }
}