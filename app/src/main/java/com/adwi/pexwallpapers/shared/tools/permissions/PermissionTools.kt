package com.adwi.pexwallpapers.shared.tools.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.util.activity
import dagger.hilt.android.qualifiers.ActivityContext
import timber.log.Timber

private const val STORAGE_REQUEST_CODE = 123
private const val TAG = "PermissionTools"

/**
 * Permission tools
 *
 * @property context
 */
class PermissionTools(
    @ActivityContext private val context: Context
) {
    val runningOOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O
    val runningNOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N
    val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    val runningSOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S

    /**
     * Storage permission
     */
    private val storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE

    /**
     * Storage permissions check
     *
     * @param body
     * @receiver
     */
    fun storagePermissionsCheck(body: () -> Unit) {
        if (isReadStoragePermissionApproved()) {
            // Check if permission is not granted
            Timber.tag(TAG).d("Permission for storage is not granted")
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
                Timber.tag(TAG).d(context.getString(R.string.checking_permissions))
                requestPermissions(
                    context.activity()!!,
                    arrayOf(storagePermission),
                    STORAGE_REQUEST_CODE
                )
            }
        } else body()
    }

    /**
     * Show rationale dialog
     *
     * @param title
     * @param message
     */
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

    /**
     * Is read storage permission approved
     *
     * @return Storage permission
     */
    private fun isReadStoragePermissionApproved(): Boolean {
        return if (runningOOrLater) {
            PackageManager.PERMISSION_GRANTED ==
                    ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.READ_EXTERNAL_STORAGE
                    )
        } else true
    }

    /**
     * Request storage permission
     *
     */
    private fun requestStoragePermission() {
        requestPermissions(context.activity()!!, arrayOf(storagePermission), STORAGE_REQUEST_CODE)
    }
}