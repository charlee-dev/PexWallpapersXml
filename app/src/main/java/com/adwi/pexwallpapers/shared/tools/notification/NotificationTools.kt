package com.adwi.pexwallpapers.shared.tools.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.adwi.pexwallpapers.MainActivity
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import com.adwi.pexwallpapers.shared.tools.notification.AppGlobalReceiver.Companion.NOTIFICATION_ID
import com.adwi.pexwallpapers.util.Constants.Companion.GROUP_AUTO
import com.adwi.pexwallpapers.util.Constants.Companion.GROUP_RECOMMENDATIONS
import com.adwi.pexwallpapers.util.Constants.Companion.WALLPAPER_ID
import com.adwi.pexwallpapers.util.runningOOrLater
import com.adwi.pexwallpapers.util.runningSOrLater
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
) {
    /**
     * Setup notifications
     *
     */
    fun setupNotifications() {
        if (runningOOrLater) {
            val wallpaperGroupName = context.getString(R.string.wallpapers)
            val appGroupName = context.getString(R.string.other)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannelGroup(
                NotificationChannelGroup(
                    WALLPAPER_GROUP_ID,
                    wallpaperGroupName
                )
            )
            notificationManager.createNotificationChannelGroup(
                NotificationChannelGroup(
                    APP_GROUP_ID,
                    appGroupName
                )
            )

            createNotificationChannel(
                context = context,
                importance = NotificationManager.IMPORTANCE_DEFAULT,
                showBadge = true,
                name = Channel.NEW_WALLPAPER.name,
                description = "Notifications for auto change wallpaper",
                group = wallpaperGroupName
            )
            createNotificationChannel(
                context = context,
                importance = NotificationManager.IMPORTANCE_DEFAULT,
                showBadge = true,
                name = Channel.RECOMMENDATIONS.name,
                description = "New wallpaper recommendations",
                group = wallpaperGroupName
            )
            createNotificationChannel(
                context = context,
                importance = NotificationManager.IMPORTANCE_HIGH,
                showBadge = false,
                name = Channel.INFO.name,
                description = "PexWallpapers info channel",
                group = appGroupName
            )
        }
    }

    /**
     * Sets up the notification channels for API 26+.
     * Note: This uses package name + channel name to create unique channelId's.
     *
     * @param context     application context
     * @param importance  importance level for the notificaiton channel
     * @param showBadge   whether the channel should have a notification badge
     * @param name        name for the notification channel
     * @param description description for the notification channel
     */
    private fun createNotificationChannel(
        context: Context,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        showBadge: Boolean = true,
        name: String,
        description: String,
        group: String
    ) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (runningOOrLater) {

            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.group = group
            channel.setShowBadge(showBadge)

            // Register the channel with the system
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


    /**
     * Creates a notification for [Wallpaper] with a full notification tap and a single action.
     *
     * @param bitmap
     * @param wallpaperId
     * @param channelId
     */

    fun createNotificationForWallpaper(
        bitmap: Bitmap,
        wallpaperId: Int,
        categoryName: String,
        photographerName: String,
        channelId: Channel,
    ) {

        // create a group notification
        val groupBuilder = buildGroupNotification(categoryName, photographerName, channelId)

        // create the pet notification
        val notificationBuilder =
            buildNotificationForWallpaper(bitmap, channelId, wallpaperId)

        // add an action to the pet notification
        val administerPendingIntent = createPendingIntentForAction(wallpaperId)

        notificationBuilder.addAction(
            R.drawable.ic_restore,
            context.getString(R.string.restore),
            administerPendingIntent
        )

        // call notify for both the group and the pet notification
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(channelId.ordinal, groupBuilder.build())
        notificationManager.notify(wallpaperId, notificationBuilder.build())
    }


    /**
     * Builds and returns the [NotificationCompat.Builder] for the group notification for a pet type.
     *
     * @param photographerName Wallpaper's photographer name for this notification
     */
    private fun buildGroupNotification(
        categoryName: String,
        photographerName: String,
        channelId: Channel
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId.name).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(categoryName)
            setContentText(
                context.getString(
                    R.string.group_notification_for,
                    categoryName
                )
            )
            setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    context.getString(
                        R.string.group_notification_for,
                        photographerName
                    )
                )
            )
            setAutoCancel(true)
            setGroupSummary(true)
            setGroup(channelId.name)
        }
    }


    /**
     * Builds and returns the NotificationCompat.Builder for the Pet notification.
     *
     * @param wallpaperId Wallpaper id for this notification
     */
    private fun buildNotificationForWallpaper(
        bitmap: Bitmap,
        channelId: Channel,
        wallpaperId: Int
    ): NotificationCompat.Builder {

        return NotificationCompat.Builder(context, channelId.name).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setAutoCancel(true)

            val largeIcon =
                BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground)

            setLargeIcon(largeIcon)
            setContentTitle("New wallpaper set")
            setContentText("PexWallpapers just set an amazing wallpaper for you")
            setGroup(channelId.name)

            setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(WALLPAPER_ID, wallpaperId)
            }

            val flag = if (runningSOrLater)
                PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_ONE_SHOT

            val pendingIntent = PendingIntent.getActivity(context, 0, intent, flag)
            setContentIntent(pendingIntent)
        }
    }

    /**
     * Creates the pending intent for the Restore Original Wallpaper Action
     * for the wallpaper notification.
     *
     * @param wallpaperId WallpaperId for this notification
     */
    private fun createPendingIntentForAction(
        wallpaperId: Int
    ): PendingIntent? {

        val administerIntent = Intent(context, AppGlobalReceiver::class.java).apply {
            action = context.getString(R.string.restore)
            putExtra(NOTIFICATION_ID, wallpaperId)
            putExtra(WALLPAPER_ID, wallpaperId)
        }

        return PendingIntent.getBroadcast(
            context,
            WALLPAPER_REQUEST_CODE,
            administerIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


//    /**
//     * Send notification
//     *
//     * @param id
//     * @param channelId
//     * @param imageUrl
//     * @param longMessage
//     */
//    @SuppressLint("UnspecifiedImmutableFlag")
//    suspend fun sendNotification(
//        id: Int,
//        channelId: Channel,
//        imageUrl: String,
//        longMessage: String = "",
//        actionName: String? = null,
//        actionExtraId: String? = null,
//        actionExtra: String? = null,
////        action: () -> Unit = {}
//    ) {
//        var requestCode = 1
//        val intentDestination: Class<*>
//
//        val smallBitmap =
//            BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground)
//
//        val notification = NotificationCompat.Builder(context, this.channelId)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//
//        when (val channel = channelId) {
//            Channel.NEW_WALLPAPER -> {
//                val largeBitmap = imageTools.getBitmapFromRemote(imageUrl)
//                intentDestination = MainActivity::class.java
//                notification
//                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
//                    .setContentTitle("New wallpaper set")
//                    .setContentText("PexWallpapers just set an amazing wallpaper for you")
//                    .setLargeIcon(smallBitmap)
//                    .setGroup(setGroup(channel))
//                    .priority = NotificationCompat.PRIORITY_DEFAULT
//            }
//            Channel.RECOMMENDATIONS -> {
//                val largeBitmap = imageTools.getBitmapFromRemote(imageUrl)
//                requestCode = 2
//                intentDestination = MainActivity::class.java
//                notification
//                    .setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
//                    .setContentTitle("Recommendations")
//                    .setContentText("PexWallpapers have some amazing wallpapers to offer for you")
//                    .setLargeIcon(smallBitmap)
//                    .setGroup(setGroup(channel))
//                    .priority = NotificationCompat.PRIORITY_DEFAULT
//            }
//            Channel.INFO -> {
//                requestCode = 3
//                intentDestination = MainActivity::class.java
//                notification
//                    .setStyle(NotificationCompat.BigTextStyle().bigText(longMessage))
//                    .setGroup(setGroup(channel))
//                    .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
//                    .setGroupSummary(true)
//            }
//        }
//
//        val intent = Intent(context, intentDestination).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            action = actionName
//            putExtra(actionExtraId, actionExtra)
//        }
//
//        val pendingIntent = if (permissionTools.runningSOrLater) {
//            PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_MUTABLE)
//        } else {
//            PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT)
//        }
//
//
//
//        notification.setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//
//        with(NotificationManagerCompat.from(context)) {
//            notify(id, notification.build())
//        }
//    }

//    fun createGroupNotification(
//        channelId: Channel = Channel.NEW_WALLPAPER,
//        title: String = channelId.name,
//    ): NotificationCompat.Builder {
//        // 1
////        val channelId = "${context.packageName}-${reminderData.type.name}"
//        return NotificationCompat.Builder(context, channelId.name).apply {
//            setSmallIcon(R.drawable.ic_launcher_foreground)
//            setContentTitle(title)
//            setContentText(context.getString(R.string.group_notification_for))
//            setStyle(
//                NotificationCompat.BigTextStyle()
//                    .bigText(context.getString(R.string.group_notification_for))
//            )
//            setAutoCancel(true)
//            setGroupSummary(true)
//            setGroup(setGroup(channelId))
//        }
//    }

//    fun createPendingIntentForAction(
//        destination: Class<*>,
//        actionName: String? = null,
//        actionExtraId: String? = null,
//        actionExtra: String? = null,
////        action: () -> Unit = {}
//    ): PendingIntent? {
//
//        val administerIntent = Intent(context, destination).apply {
//            action = actionName
//            putExtra(actionExtraId, actionExtra)
//        }
//
//        return PendingIntent.getBroadcast(
//            context,
//            WALLPAPER_REQUEST_CODE,
//            administerIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//    }

    private fun setChannelGroup(channelId: Channel) = when (channelId) {
        Channel.NEW_WALLPAPER -> GROUP_AUTO
        Channel.RECOMMENDATIONS -> GROUP_RECOMMENDATIONS
        Channel.INFO -> GROUP_AUTO
    }
}

private const val WALLPAPER_REQUEST_CODE = 2019
private const val WALLPAPER_GROUP_ID = "wallpaper_group"
private const val APP_GROUP_ID = "app_group"