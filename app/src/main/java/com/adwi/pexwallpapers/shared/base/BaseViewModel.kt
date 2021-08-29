package com.adwi.pexwallpapers.shared.base

import androidx.lifecycle.ViewModel
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d


// Add @HiltViewModel in each viewModel
abstract class BaseViewModel : ViewModel() {

    val TAG = javaClass.simpleName

    init {
        Timber.tag(TAG).d { "Initialized" }
    }
}