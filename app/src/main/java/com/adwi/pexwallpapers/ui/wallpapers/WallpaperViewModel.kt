package com.adwi.pexwallpapers.ui.wallpapers

import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.WallpaperRepository
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.local.entity.defaultSettings
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import com.adwi.pexwallpapers.util.Resource
import com.adwi.pexwallpapers.util.onIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : BaseViewModel() {

    // Hot flow - produces value no matter if there is collector or not
    private val eventChannel = Channel<Event>()
    val events = eventChannel.receiveAsFlow()

    private val refreshTriggerChannel = Channel<Refresh>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    var pendingScrollToTopAfterRefresh = false

    val wallpaperList = refreshTrigger.flatMapLatest { refresh ->
        repository.getCuratedWallpapers(
            refresh == Refresh.FORCE,
            onFetchSuccess = {
                pendingScrollToTopAfterRefresh = true
            },
            onFetchRemoteFailed = { t ->
                onIO { eventChannel.send(Event.ShowErrorMessage(t)) }
            }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {
        onIO {
            repository.deleteNonFavoriteWallpapersOlderThan(
                System.currentTimeMillis() - TimeUnit.DAYS.toMillis(14)
            )
        }
    }

    fun onStart() {
        if (wallpaperList.value !is Resource.Loading)
            onIO {
                refreshTriggerChannel.send(Refresh.NORMAL)
            }
        onIO {
            if (repository.getSettings() == null) {
                repository.insertSettings(defaultSettings)
            }
        }
    }

    fun onManualRefresh() {
        if (wallpaperList.value !is Resource.Loading)
            onIO {
                refreshTriggerChannel.send(Refresh.FORCE)
            }
    }

    fun onFavoriteClick(wallpaper: Wallpaper) {
        val isFavorite = wallpaper.isFavorite
        wallpaper.isFavorite = !isFavorite
        onIO {
            repository.updateWallpaper(wallpaper)
        }
    }

    enum class Refresh {
        FORCE, NORMAL
    }

    sealed class Event {
        data class ShowErrorMessage(val error: Throwable) : Event()
    }
}