package com.adwi.pexwallpapers.shared.base

import androidx.lifecycle.ViewModel
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d

abstract class BaseViewModel : ViewModel() {

    private val TAG = javaClass.simpleName

    init {
        Timber.tag(TAG).d { "Initialized" }
    }
}