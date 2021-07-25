package com.adwi.pexwallpapers.ui.favorites

import android.app.Application
import com.adwi.pexwallpapers.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(app: Application) : BaseViewModel(app)