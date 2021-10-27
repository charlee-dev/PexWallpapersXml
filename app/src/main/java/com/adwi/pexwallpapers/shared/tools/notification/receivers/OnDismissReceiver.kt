package com.adwi.pexwallpapers.shared.tools.notification.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.adwi.pexwallpapers.shared.tools.image.ImageTools
import com.adwi.pexwallpapers.shared.tools.notification.ACTION_AUTO
import com.adwi.pexwallpapers.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnDismissReceiver : BroadcastReceiver() {

    @Inject
    lateinit var imageTools: ImageTools

    override fun onReceive(context: Context?, intent: Intent?) {
        val wallpaperId = intent?.getStringExtra(ACTION_AUTO)
        Toast.makeText(context, "test $wallpaperId", Toast.LENGTH_SHORT).show()
        wallpaperId?.let {
            imageTools.deleteBackupBitmap(wallpaperId)
            showToast(context!!.applicationContext, "backup $wallpaperId deleted")
        }
    }
}