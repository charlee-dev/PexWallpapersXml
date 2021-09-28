package com.adwi.pexwallpapers.shared.tools.permissions

interface PermissionToolsInterface {

    fun storagePermissionsCheck(body: () -> Unit)

    fun showRationaleDialog(
        title: String,
        message: String
    )

    fun isReadStoragePermissionApproved(): Boolean

    fun requestStoragePermission()
}