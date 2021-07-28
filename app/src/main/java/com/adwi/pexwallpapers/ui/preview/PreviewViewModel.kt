package com.adwi.pexwallpapers.ui.preview

import com.adwi.pexwallpapers.data.WallpaperRepository
import com.adwi.pexwallpapers.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(repository: WallpaperRepository) : BaseViewModel()