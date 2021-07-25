package com.adwi.pexwallpapers.ui.preview

import androidx.lifecycle.ViewModel
import com.adwi.pexwallpapers.data.repository.PreviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(repository: PreviewRepository) : ViewModel()