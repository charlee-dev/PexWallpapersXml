package com.adwi.pexwallpapers.base

import android.content.res.Resources
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.repository.Repository
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d

abstract class BaseRepository : Repository {

    companion object {
        private val TAG = this::class.java.simpleName
    }

    private val resources = Resources.getSystem()

    init {
        Timber.tag(TAG).d { resources.getString(R.string.init_class) }
    }
}