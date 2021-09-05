package com.adwi.pexwallpapers.shared.tools

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.adwi.pexwallpapers.MainActivity
import com.adwi.pexwallpapers.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationTools @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharingTools: SharingTools
) {

    private val CHANNEL_ID = "channel_id_pex_new_wallpaper"
    private val notificationNewWallpaperId = 101
    private val REQUEST_CODE_NEW_WALLPAPER = 1

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "New wallpaper notification"
            val descriptionText =
                "Receive notification every time PexWallpapers sets scheduled new wallpaper in background"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    suspend fun sendNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, REQUEST_CODE_NEW_WALLPAPER, intent, 0)

        val smallBitmap =
            sharingTools.getBitmap("https://images.pexels.com/photos/2014422/pexels-photo-2014422.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280")
        val largeBitmap =
            sharingTools.getBitmap("https://images.pexels.com/photos/2014422/pexels-photo-2014422.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=627&w=1200")

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Test notification")
            .setContentText("test description")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLargeIcon(smallBitmap)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(context)) {
            notify(notificationNewWallpaperId, builder.build())
        }
    }
}