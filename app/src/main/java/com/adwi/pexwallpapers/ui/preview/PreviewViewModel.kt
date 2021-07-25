package com.adwi.pexwallpapers.ui.preview

import android.app.Application
import com.adwi.pexwallpapers.base.BaseViewModel
import com.adwi.pexwallpapers.data.repository.PreviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    app: Application,
    repository: PreviewRepository
) : BaseViewModel(app)