package com.adwi.pexwallpapers.ui.search

import android.app.Application
import com.adwi.pexwallpapers.base.BaseViewModel
import com.adwi.pexwallpapers.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    app: Application,
    repository: SearchRepository
) : BaseViewModel(app)