package com.adwi.pexwallpapers.ui.favorites

import androidx.lifecycle.ViewModel
import com.adwi.pexwallpapers.data.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(repository: FavoritesRepository) : ViewModel()