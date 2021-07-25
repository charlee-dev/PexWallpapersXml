package com.adwi.pexwallpapers.ui.search

import androidx.lifecycle.ViewModel
import com.adwi.pexwallpapers.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(repository: SearchRepository) : ViewModel()