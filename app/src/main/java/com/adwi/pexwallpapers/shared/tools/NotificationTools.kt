package com.adwi.pexwallpapers.shared.tools

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
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
    private val imageTools: ImageTools
) {
    private lateinit var CHANNEL_ID: String

    private val wallpaperGroupId = "wallpaper_group"
    private val appGroupId = "app_group"

    fun setupNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val wallpaperGroupName = context.getString(R.string.wallpapers)
            val appGroupName = context.getString(R.string.other)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannelGroup(
                NotificationChannelGroup(
                    wallpaperGroupId,
                    wallpaperGroupName
                )
            )
            notificationManager.createNotificationChannelGroup(
                NotificationChannelGroup(
                    appGroupId,
                    appGroupName
                )
            )
            createNotificationChannel(Channel.NEW_WALLPAPER)
            createNotificationChannel(Channel.RECOMMENDATIONS)
            createNotificationChannel(Channel.INFO)
        }
    }


    private fun createNotificationChannel(channel: Channel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var name = ""
            var importance = 0
            val channelGroup: String
            when (channel) {
                Channel.NEW_WALLPAPER -> {
                    CHANNEL_ID = "pex_new_wallpaper"
                    name = "New wallpaper"
                    importance = NotificationManager.IMPORTANCE_DEFAULT
                    channelGroup = wallpaperGroupId
                }
                Channel.RECOMMENDATIONS -> {
                    CHANNEL_ID = "pex_recommendations"
                    name = "Recommendations"
                    importance = NotificationManager.IMPORTANCE_DEFAULT
                    channelGroup = wallpaperGroupId
                }
                Channel.INFO -> {
                    CHANNEL_ID = "info"
                    name = "Info"
                    importance = NotificationManager.IMPORTANCE_HIGH
                    channelGroup = appGroupId
                }
            }
            val notificationChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationChannel.group = channelGroup
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    suspend fun sendNotification(channel: Channel, imageUrl: String, longMessage: String) {
        var notificationNewWallpaperId = 101
        var requestCode = 1
        val intentDestination: Class<*>

//         TODO() add deep link to set wallpaper
        val smallBitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)

        when (channel) {
            Channel.NEW_WALLPAPER -> {
                val largeBitmap = imageTools.getBitmapFromRemote(imageUrl)
                intentDestination = MainActivity::class.java
                builder
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
                    .setContentTitle("New wallpaper set")
                    .setContentText("PexWallpapers just set an amazing wallpaper for you")
                    .setLargeIcon(smallBitmap)
                    .priority = NotificationCompat.PRIORITY_DEFAULT
            }
            Channel.RECOMMENDATIONS -> {
                val largeBitmap = imageTools.getBitmapFromRemote(imageUrl)
                notificationNewWallpaperId = 102
                requestCode = 2
                intentDestination = MainActivity::class.java
                builder
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
                    .setContentTitle("Recommendations")
                    .setContentText("PexWallpapers have some amazing wallpapers to offer for you")
                    .setLargeIcon(smallBitmap)
                    .priority = NotificationCompat.PRIORITY_DEFAULT
            }
            Channel.INFO -> {
                notificationNewWallpaperId = 103
                requestCode = 3
                intentDestination = MainActivity::class.java
                builder.setStyle(NotificationCompat.BigTextStyle().bigText(longMessage))
            }
        }
        val intent = Intent(context, intentDestination).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, requestCode, intent, 0)

        builder.setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationNewWallpaperId, builder.build())
        }
    }
}