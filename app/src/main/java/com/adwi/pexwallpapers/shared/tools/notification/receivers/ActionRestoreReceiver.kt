package com.adwi.pexwallpapers.shared.tools.notification.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.shared.tools.notification.ACTION_AUTO
import com.adwi.pexwallpapers.shared.work.WorkTools
import com.adwi.pexwallpapers.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ActionRestoreReceiver : BroadcastReceiver() {

    @Inject
    lateinit var workTools: WorkTools

    override fun onReceive(context: Context?, intent: Intent?) {
        val wallpaperId = intent?.getStringExtra(ACTION_AUTO)

        wallpaperId?.let {
            workTools.createRestoreWallpaperWork(wallpaperId)

            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.cancel(wallpaperId.toInt())

            showToast(context, context.getString(R.string.previous_wallpaper_has_been_restored))
        }
    }
}