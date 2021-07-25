package com.adwi.pexwallpapers.ui.wallpapers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.data.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(repository: WallpaperRepository) : ViewModel() {

    private val wallpaperListFlow = MutableStateFlow<List<Wallpaper>>(emptyList())
    val wallpaperList: Flow<List<Wallpaper>> = wallpaperListFlow

    init {
        viewModelScope.launch {
            val wallpapers = repository.getCuratedWallpapers()
            wallpaperListFlow.value = wallpapers
        }
    }
}