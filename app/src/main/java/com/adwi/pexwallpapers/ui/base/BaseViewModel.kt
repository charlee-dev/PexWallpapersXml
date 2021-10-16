package com.adwi.pexwallpapers.ui.base

import androidx.lifecycle.ViewModel
import timber.log.Timber


// Add @HiltViewModel in each viewModel
abstract class BaseViewModel : ViewModel() {

    val TAG = javaClass.simpleName

    init {
        Timber.tag(TAG).d("Initialized")
    }
}