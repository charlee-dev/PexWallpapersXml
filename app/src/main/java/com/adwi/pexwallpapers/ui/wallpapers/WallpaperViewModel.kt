package com.adwi.pexwallpapers.ui.wallpapers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.repository.WallpaperRepository
import com.adwi.pexwallpapers.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(repository: WallpaperRepository) : ViewModel() {

    // Hot flow - produces value no matter if there is collector or not
    private val refreshTriggerChannel = Channel<Unit>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    val wallpaperList = refreshTrigger.flatMapLatest {
        repository.getCuratedWallpapers()
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onStart() {
        if (wallpaperList.value !is Resource.Loading)
            viewModelScope.launch {
                refreshTriggerChannel.send(Unit)
            }
    }

    fun onManualRefresh() {
        if (wallpaperList.value !is Resource.Loading)
            viewModelScope.launch {
                refreshTriggerChannel.send(Unit)
            }
    }

}