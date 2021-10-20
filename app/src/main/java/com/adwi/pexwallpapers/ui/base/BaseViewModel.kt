package com.adwi.pexwallpapers.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber


// Add @HiltViewModel in each viewModel
abstract class BaseViewModel : ViewModel() {

    val TAG = javaClass.simpleName

    val isStoragePermissionGranted = MutableStateFlow(false)

    init {
        Timber.tag(TAG).d("Initialized")
    }

    fun setIsStoragePermissionGranted(enabled: Boolean) {
        isStoragePermissionGranted.value = enabled
    }
}