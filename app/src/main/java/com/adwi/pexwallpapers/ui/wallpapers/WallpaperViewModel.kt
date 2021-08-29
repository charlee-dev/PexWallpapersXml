package com.adwi.pexwallpapers.ui.wallpapers

import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.local.entity.defaultSettings
import com.adwi.pexwallpapers.data.repository.interfaces.FavoritesRepositoryInterface
import com.adwi.pexwallpapers.data.repository.interfaces.SettingsRepositoryInterface
import com.adwi.pexwallpapers.data.repository.interfaces.WallpaperRepositoryInterface
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import com.adwi.pexwallpapers.util.Resource
import com.adwi.pexwallpapers.util.onIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepositoryInterface,
    private val favoritesRepository: FavoritesRepositoryInterface,
    private val settingsRepository: SettingsRepositoryInterface
) : BaseViewModel() {

    // Hot flow - produces value no matter if there is collector or not
    private val eventChannel = Channel<Event>()
    val events = eventChannel.receiveAsFlow()

    private val refreshTriggerChannel = Channel<Refresh>()
    val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    var pendingScrollToTopAfterRefresh = false

    val wallpaperList = refreshTrigger.flatMapLatest { refresh ->
        getWallpapers(
            refresh == Refresh.FORCE,
            { pendingScrollToTopAfterRefresh = true },
            { t ->
                onIO { eventChannel.send(Event.ShowErrorMessage(t)) }
            }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {
        onIO {
            favoritesRepository.deleteNonFavoriteWallpapersOlderThan(
                System.currentTimeMillis() - TimeUnit.DAYS.toMillis(14)
            )
        }
    }

    fun getWallpapers(
        refresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchRemoteFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Wallpaper>>> = flow {
        wallpaperRepository.getCuratedWallpapers(
            refresh,
            onFetchSuccess = onFetchSuccess,
            onFetchRemoteFailed = onFetchRemoteFailed
        )
    }

    fun onStart() {
        if (wallpaperList.value !is Resource.Loading)
            onIO {
                refreshTriggerChannel.send(Refresh.NORMAL)
            }
        onIO {
            if (settingsRepository.getSettings() == null) {
                settingsRepository.insertSettings(defaultSettings)
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