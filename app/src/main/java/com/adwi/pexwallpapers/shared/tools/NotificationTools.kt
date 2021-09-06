package com.adwi.pexwallpapers.shared.tools

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.adwi.pexwallpapers.MainActivity
import com.adwi.pexwallpapers.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

enum class Channel {
    NEW_WALLPAPER, RECOMMENDATIONS, INFO
}

class NotificationTools @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharingTools: SharingTools
) {

    private lateinit var CHANNEL_ID: String


    fun createNotificationChannel(channel: Channel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var name = ""
            var importance = 0
            when (channel) {
                Channel.NEW_WALLPAPER -> {
                    CHANNEL_ID = "pex_new_wallpaper"
                    name = "New wallpaper"
                    importance = NotificationManager.IMPORTANCE_DEFAULT
                }
                Channel.RECOMMENDATIONS -> {
                    CHANNEL_ID = "pex_recommendations"
                    name = "Recommendations"
                    importance = NotificationManager.IMPORTANCE_DEFAULT
                }
                Channel.INFO -> {
                    CHANNEL_ID = "info"
                    name = "Info"
                    importance = NotificationManager.IMPORTANCE_HIGH
                }
            }
            val notificationChannel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    suspend fun sendNotification(channel: Channel, imageUrl: String) {
        var notificationNewWallpaperId = 101
        var requestCode = 1

//         TODO() add deep link to set wallpaper
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val smallBitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground)

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, requestCode, intent, 0)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Test notification")
            .setContentText("test description")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLargeIcon(smallBitmap)
            .setContentIntent(pendingIntent)

        when (channel) {
            Channel.NEW_WALLPAPER -> {
                val largeBitmap =
                    sharingTools.getBitmap(imageUrl)
                builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
            }
            Channel.RECOMMENDATIONS -> {
                notificationNewWallpaperId = 102
                requestCode = 2
            }
            Channel.INFO -> {
                notificationNewWallpaperId = 103
                requestCode = 3
                builder.setStyle(NotificationCompat.BigTextStyle().bigText("asdsd"))
            }
        }

        with(NotificationManagerCompat.from(context)) {
            notify(notificationNewWallpaperId, builder.build())
        }
    }
}