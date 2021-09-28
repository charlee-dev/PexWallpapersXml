package com.adwi.pexwallpapers.shared.tools.notification

interface NotificationToolsInterface {

    fun setupNotifications()

    fun createNotificationChannel(channel: Channel)

    suspend fun sendNotification(
        id: Int,
        channel: Channel,
        imageUrl: String,
        longMessage: String = ""
    )
}