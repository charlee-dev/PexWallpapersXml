package com.adwi.pexwallpapers.shared.tools.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import com.adwi.pexwallpapers.shared.tools.wallpaper.WallpaperSetter
import com.adwi.pexwallpapers.util.Constants.Companion.WALLPAPER_ID
import javax.inject.Inject

/**
 * Handles any global application broadcasts.
 * <p>
 * Note: this really only handles the action from the
 * pet notification to administer the medicine but it could be used for any other actions.
 */
class AppGlobalReceiver: BroadcastReceiver() {

    @Inject
    lateinit var wallpaperSetter: WallpaperSetter

    @Inject
    lateinit var imageTools: ImageTools

    companion object {
        const val NOTIFICATION_ID = "notification_id"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null && intent.action != null) {

            // Handle the action to set the Medicine Administered
            if (intent.action!!.equals(context.getString(R.string.restore), ignoreCase = true)) {

                val extras = intent.extras
                if (extras != null) {

                    // Get extras
                    val notificationId = extras.getInt(NOTIFICATION_ID)
                    val wallpaperId = extras.getInt(WALLPAPER_ID)

                    // Get bitmap
                    val bitmap = imageTools.bitmapFromLocal(wallpaperId)

                    // Set wallpaper
                    wallpaperSetter.setWallpaper(
                        bitmap = bitmap,
                        setHomeScreen = true,
                        setLockScreen = false
                    )

                    // Delete backup image
                    imageTools.deleteBackupBitmap(wallpaperId)

                    // finally, cancel the notification
                    if (notificationId != -1) {
                        val notificationManager = NotificationManagerCompat.from(context)
                        notificationManager.cancel(notificationId)
                        notificationManager.cancelAll() // testing
                    }
                }
            }
        }
    }
}