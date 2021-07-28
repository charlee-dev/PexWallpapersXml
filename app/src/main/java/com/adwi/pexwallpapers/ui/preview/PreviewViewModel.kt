package com.adwi.pexwallpapers.ui.preview

import androidx.lifecycle.ViewModel
import com.adwi.pexwallpapers.data.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(repository: WallpaperRepository) : ViewModel()