package com.adwi.pexwallpapers.ui.wallpapers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(repository: WallpaperRepository) : ViewModel() {

    val wallpaperList = repository.getCuratedWallpapers()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
}