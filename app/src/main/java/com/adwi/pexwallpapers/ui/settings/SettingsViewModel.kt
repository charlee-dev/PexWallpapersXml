package com.adwi.pexwallpapers.ui.settings

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Settings
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.local.entity.defaultSettings
import com.adwi.pexwallpapers.data.repository.FavoritesRepository
import com.adwi.pexwallpapers.data.repository.interfaces.SettingsRepositoryInterface
import com.adwi.pexwallpapers.di.IoDispatcher
import com.adwi.pexwallpapers.shared.tools.Channel
import com.adwi.pexwallpapers.shared.tools.NotificationTools
import com.adwi.pexwallpapers.shared.tools.SharingTools
import com.adwi.pexwallpapers.shared.work.NewWallpaperWork
import com.adwi.pexwallpapers.shared.work.WORKER_NEW_WALLPAPER_IMAGE_URL_FULL
import com.adwi.pexwallpapers.shared.work.WORKER_NEW_WALLPAPER_NOTIFICATION_IMAGE
import com.adwi.pexwallpapers.ui.base.BaseViewModel
import com.adwi.pexwallpapers.util.onDispatcher
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepositoryInterface,
    private val favoritesRepository: FavoritesRepository,
    private val sharingTools: SharingTools,
    private val notificationTools: NotificationTools,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext context: Context
) : BaseViewModel() {

    private var favoriteWallpapers: List<Wallpaper>? = null

    private val application = getApplication(context)
    private val workManager = WorkManager.getInstance(application)

    val currentChangePeriodType =
        MutableStateFlow(R.id.hours_radio_button)

    private val _currentSettings = MutableStateFlow(defaultSettings)
    val currentSettings: StateFlow<Settings> = _currentSettings

    init {
        notificationTools.setupNotifications()
        onDispatcher(ioDispatcher) {
            _currentSettings.value = repository.getSettings()
        }
        onDispatcher(ioDispatcher) {
            favoriteWallpapers = getFavoriteWallpapers()
        }
    }

    fun updatePushNotification(enabled: Boolean) {
        onDispatcher(ioDispatcher) {
            repository.updatePushNotification(enabled)
            if (enabled) {
                notificationTools.sendNotification(
                    101,
                    Channel.NEW_WALLPAPER,
                    "https://images.pexels.com/photos/5273316/pexels-photo-5273316.jpeg?cs=srgb&dl=pexels-julia-volk-5273316.jpg&fm=jpg",
                    ""
                )
            }
        }
    }

    fun updateNewWallpaperSet(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateNewWallpaperSet(enabled) }
    }

    fun updateWallpaperRecommendations(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateWallpaperRecommendations(enabled) }
    }

    fun updateAutoChangeWallpaper(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateAutoChangeWallpaper(enabled) }
    }

    fun updateDownloadOverWiFi(enabled: Boolean) {
        onDispatcher(ioDispatcher) { repository.updateDownloadOverWiFi(enabled) }
    }

    fun updateChangePeriodType(radioButton: Int) {
        currentChangePeriodType.value = radioButton
        onDispatcher(ioDispatcher) { repository.updateChangePeriodType(radioButton) }
    }

    fun updateChangePeriodValue(periodValue: Float) {
        onDispatcher(ioDispatcher) { repository.updateChangePeriodValue(periodValue) }
    }

    fun resetSettings() {
        onDispatcher(ioDispatcher) { repository.resetAllSettings() }
    }

    fun contactSupport() {
        sharingTools.contactSupport()
    }

    private suspend fun getFavoriteWallpapers() = favoritesRepository.getAllFavorites().first()

    private fun createDataForNewWallpaperWorker(wallpaper: Wallpaper): Data {
        val builder = Data.Builder()
        favoriteWallpapers?.let {
            builder.putString(WORKER_NEW_WALLPAPER_IMAGE_URL_FULL, wallpaper.src?.portrait)
            builder.putString(WORKER_NEW_WALLPAPER_NOTIFICATION_IMAGE, wallpaper.src?.tiny)
        }
        return builder.build()
    }

    private fun createNewWallpaperWork() {
        val work = OneTimeWorkRequestBuilder<NewWallpaperWork>()
            .setInputData(createDataForNewWallpaperWorker(favoriteWallpapers!!.first()))
            .build()

        workManager.enqueue(work)
    }

    override fun onCleared() {
        createNewWallpaperWork()
        super.onCleared()
    }
}