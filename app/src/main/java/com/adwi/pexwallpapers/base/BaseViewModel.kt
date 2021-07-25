package com.adwi.pexwallpapers.base

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.util.SingleLiveEvent
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    companion object {
        private val TAG = this::class.java.simpleName
    }

    private val resources = Resources.getSystem()

    //    val navigationCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()
    val showErrorMessage: SingleLiveEvent<String> = SingleLiveEvent()
    val showSnackBar: SingleLiveEvent<String> = SingleLiveEvent()
    val showSnackBarInt: SingleLiveEvent<Int> = SingleLiveEvent()
    val showToast: SingleLiveEvent<String> = SingleLiveEvent()
    val showLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        Timber.tag(TAG).d { resources.getString(R.string.init_class) }
    }
}