package com.adwi.pexwallpapers.ui.setwallpaper

import androidx.lifecycle.SavedStateHandle
import com.adwi.pexwallpapers.data.repository.WallpaperRepository
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SetWallpaperViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: WallpaperRepository
) : BaseViewModel()