package com.adwi.pexwallpapers.shared.tools

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.util.activity
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import dagger.hilt.android.qualifiers.ActivityContext

const val STORAGE_REQUEST_CODE = 123
private const val TAG = "PermissionTools"

class PermissionTools(
    @ActivityContext private val context: Context
) {
    companion object {
        val runningOOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O
        val runningNOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N
        val runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    }
    private val storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE

    fun storagePermissionsCheck(body: () -> Unit) {
        if (isReadStoragePermissionApproved()) {
            // Check if permission is not granted
            Timber.tag(TAG).d { "Permission for contacts is not granted" }
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context.activity()!!,
                    storagePermission
                )
            ) {
                showRationaleDialog(
                    context.getString(R.string.rationale_title),
                    context.getString(R.string.rationale_desc)
                )
            } else {
                Timber.tag(TAG).d { "Checking permission" }
                requestPermissions(
                    context.activity()!!,
                    arrayOf(storagePermission),
                    STORAGE_REQUEST_CODE
                )
            }
        } else body()
    }

    private fun showRationaleDialog(
        title: String,
        message: String
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ ->
                requestStoragePermission()
            }
        builder.create().show()
    }

    fun isReadStoragePermissionApproved(): Boolean {
        return if (runningOOrLater) {
            PackageManager.PERMISSION_GRANTED ==
                    ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.READ_EXTERNAL_STORAGE
                    )
        } else true
    }

    private fun requestStoragePermission() {
        requestPermissions(context.activity()!!, arrayOf(storagePermission), STORAGE_REQUEST_CODE)
    }
}