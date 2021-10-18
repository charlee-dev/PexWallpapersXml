package com.adwi.pexwallpapers

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.adwi.pexwallpapers.data.local.entity.defaultSettings
import com.adwi.pexwallpapers.data.local.entity.suggestionNameList
import com.adwi.pexwallpapers.data.repository.SettingsRepository
import com.adwi.pexwallpapers.data.repository.SuggestionsRepository
import com.adwi.pexwallpapers.shared.tools.notification.NotificationTools
import com.adwi.pexwallpapers.util.TypeConverter
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@DelicateCoroutinesApi
@HiltAndroidApp
class PexApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var notificationTools: NotificationTools

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var suggestionsRepository: SuggestionsRepository

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        GlobalScope.launch(Dispatchers.IO) {
            initSettings()
            initDefaultSuggestionList()
        }
    }

    private suspend fun initSettings() {
        notificationTools.setupNotifications()
        val settings = settingsRepository.getSettings().first()
        if (settings == null) {
            Timber.d("INIT SETTINGS")
            settingsRepository.insertSettings(defaultSettings)
        }
    }

    private suspend fun initDefaultSuggestionList() {
        suggestionsRepository.insertAllSuggestions(
            TypeConverter.defaultSuggestionNameListToSuggestions(suggestionNameList)
        )
    }
}