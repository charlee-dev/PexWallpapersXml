package com.adwi.pexwallpapers.ui.base

import androidx.lifecycle.ViewModel
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import kotlinx.coroutines.flow.MutableStateFlow


abstract class BaseViewModel : ViewModel() {

    val TAG: String = javaClass.simpleName

    val snackBarMessage = MutableStateFlow("")
    val toastMessage = MutableStateFlow("")

    init {
        Timber.tag(TAG).d { "Initialized" }
    }
}