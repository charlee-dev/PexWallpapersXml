package com.adwi.pexwallpapers.ui.wallpapers

import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.local.entity.defaultSettings
import com.adwi.pexwallpapers.data.repository.interfaces.FavoritesRepositoryInterface
import com.adwi.pexwallpapers.data.repository.interfaces.SettingsRepositoryInterface
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import com.adwi.pexwallpapers.di.IoDispatcher
import com.adwi.pexwallpapers.ui.base.BaseViewModel
import com.adwi.pexwallpapers.util.Resource
import com.adwi.pexwallpapers.util.onDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepositoryInterface,
    private val favoritesRepository: FavoritesRepositoryInterface,
    private val settingsRepository: SettingsRepositoryInterface,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    // Hot flow - produces value no matter if there is collector or not
    private val eventChannel = Channel<Event>()
    val events = eventChannel.receiveAsFlow()

    private val refreshTriggerChannel = Channel<Refresh>()
    val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    var pendingScrollToTopAfterRefresh = false

    val wallpaperList = refreshTrigger.flatMapLatest { refresh ->
        getWallpapers(refresh == Refresh.FORCE)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {
        onDispatcher(ioDispatcher) {
            favoritesRepository.deleteNonFavoriteWallpapersOlderThan(
                System.currentTimeMillis() - TimeUnit.DAYS.toMillis(14)
            )
        }
    }

    fun getWallpapers(refresh: Boolean) = wallpaperRepository.getCuratedWallpapers(
        forceRefresh = refresh,
        onFetchSuccess = { pendingScrollToTopAfterRefresh = true },
        onFetchRemoteFailed = { t ->
            onDispatcher(ioDispatcher) { eventChannel.send(Event.ShowErrorMessage(t)) }
        }
    )

    fun onStart() {
        if (wallpaperList.value !is Resource.Loading)
            onDispatcher(ioDispatcher) {
                refreshTriggerChannel.send(Refresh.NORMAL)
            }
        onDispatcher(ioDispatcher) {
            if (settingsRepository.getSettings() == null) {
                settingsRepository.insertSettings(defaultSettings)
            }
        }
    }

    fun onManualRefresh() {
        if (wallpaperList.value !is Resource.Loading)
            onDispatcher(ioDispatcher) {
                refreshTriggerChannel.send(Refresh.FORCE)
            }
    }

    fun onFavoriteClick(wallpaper: Wallpaper) {
        val isFavorite = wallpaper.isFavorite
        wallpaper.isFavorite = !isFavorite
        onDispatcher(ioDispatcher) {
            wallpaperRepository.updateWallpaper(wallpaper)
        }
    }
}

enum class Refresh {
    FORCE, NORMAL
}

sealed class Event {
    data class ShowErrorMessage(val error: Throwable) : Event()
}