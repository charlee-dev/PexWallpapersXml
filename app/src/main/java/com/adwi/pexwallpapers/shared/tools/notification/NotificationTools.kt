package com.adwi.pexwallpapers.shared.tools.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color.RED
import android.media.AudioAttributes
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.adwi.pexwallpapers.MainActivity
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import com.adwi.pexwallpapers.shared.tools.permissions.PermissionTools
import com.adwi.pexwallpapers.util.Constants.Companion.GROUP_AUTO
import com.adwi.pexwallpapers.util.Constants.Companion.GROUP_INFO
import com.adwi.pexwallpapers.util.Constants.Companion.GROUP_RECOMMENDATIONS
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Defines notification channels
 */
enum class Channel {
    NEW_WALLPAPER,
    RECOMMENDATIONS,
    INFO
}

/**
 * Notification tools
 *
 * @property context
 * @property imageTools
 * @property permissionTools
 * @constructor Create empty Notification tools
 */
@SuppressLint("NewApi")
class NotificationTools @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageTools: ImageTools,
    private val permissionTools: PermissionTools
) {
    /**
     * Channel id
     */
    private lateinit var channelId: String

    /**
     * Wallpaper group id
     */
    private val wallpaperGroupId = "wallpaper_group"

    /**
     * App group id
     */
    private val appGroupId = "app_group"


    /**
     * Setup notifications
     *
     */
    fun setupNotifications() {
        if (permissionTools.runningOOrLater) {
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

    /**
     * Create notification channel
     *
     * @param channel
     */
    private fun createNotificationChannel(channel: Channel) {
        if (permissionTools.runningOOrLater) {
            var name = ""
            var importance = 0
            val channelGroup: String
            when (channel) {
                Channel.NEW_WALLPAPER -> {
                    channelId = "pex_new_wallpaper"
                    name = "New wallpaper"
                    importance = NotificationManager.IMPORTANCE_DEFAULT
                    channelGroup = wallpaperGroupId
                }
                Channel.RECOMMENDATIONS -> {
                    channelId = "pex_recommendations"
                    name = "Recommendations"
                    importance = NotificationManager.IMPORTANCE_DEFAULT
                    channelGroup = wallpaperGroupId
                }
                Channel.INFO -> {
                    channelId = "info"
                    name = "Info"
                    importance = NotificationManager.IMPORTANCE_HIGH
                    channelGroup = appGroupId
                }
            }
            val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
            val notificationChannel = NotificationChannel(channelId, name, importance)
            notificationChannel.apply {
                group = channelGroup
                enableLights(true)
                lightColor = RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                setSound(ringtoneManager, audioAttributes)
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun createGroupNotification(
        channelId: Channel = Channel.NEW_WALLPAPER,
        title: String = channelId.name,
    ): NotificationCompat.Builder {
        // 1
//        val channelId = "${context.packageName}-${reminderData.type.name}"
        return NotificationCompat.Builder(context, channelId.name).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(context.getString(R.string.group_notification_for))
            setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(context.getString(R.string.group_notification_for))
            )
            setAutoCancel(true)
            setGroupSummary(true)
            setGroup(setGroup(channelId))
        }
    }

    /**
     * Send notification
     *
     * @param id
     * @param channelId
     * @param imageUrl
     * @param longMessage
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    suspend fun sendNotification(
        id: Int,
        channelId: Channel,
        imageUrl: String,
        longMessage: String = "",
        actionName: String? = null,
        action: () -> Unit = {}
    ) {
        var requestCode = 1
        val intentDestination: Class<*>

        val smallBitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground)

        val notification = NotificationCompat.Builder(context, this.channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)

        when (val channel = channelId) {
            Channel.NEW_WALLPAPER -> {
                val largeBitmap = imageTools.getBitmapFromRemote(imageUrl)
                intentDestination = MainActivity::class.java
                notification
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
                    .setContentTitle("New wallpaper set")
                    .setContentText("PexWallpapers just set an amazing wallpaper for you")
                    .setLargeIcon(smallBitmap)
                    .setGroup(setGroup(channel))
                    .priority = NotificationCompat.PRIORITY_DEFAULT
            }
            Channel.RECOMMENDATIONS -> {
                val largeBitmap = imageTools.getBitmapFromRemote(imageUrl)
                requestCode = 2
                intentDestination = MainActivity::class.java
                notification
                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
                    .setContentTitle("Recommendations")
                    .setContentText("PexWallpapers have some amazing wallpapers to offer for you")
                    .setLargeIcon(smallBitmap)
                    .setGroup(setGroup(channel))
                    .priority = NotificationCompat.PRIORITY_DEFAULT
            }
            Channel.INFO -> {
                requestCode = 3
                intentDestination = MainActivity::class.java
                notification
                    .setStyle(NotificationCompat.BigTextStyle().bigText(longMessage))
                    .setGroup(setGroup(channel))
                    .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                    .setGroupSummary(true)
            }
        }
        val intent = Intent(context, intentDestination).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = if (permissionTools.runningSOrLater) {
            PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        notification.setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(id, notification.build())
        }
    }

    private fun setGroup(channelId: Channel) = when (channelId) {
        Channel.NEW_WALLPAPER -> GROUP_AUTO
        Channel.RECOMMENDATIONS -> GROUP_RECOMMENDATIONS
        Channel.INFO -> GROUP_AUTO
    }
}