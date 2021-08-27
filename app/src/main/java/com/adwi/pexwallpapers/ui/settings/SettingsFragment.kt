package com.adwi.pexwallpapers.ui.settings

import android.view.MenuItem
import androidx.fragment.app.viewModels
import com.adwi.pexwallpapers.databinding.FragmentSettingsBinding
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.ui.wallpapers.WallpaperViewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding, Any>(
    inflate = FragmentSettingsBinding::inflate
) {

    override val viewModel: WallpaperViewModel by viewModels()

    override fun setupToolbar() {

    }


    override fun setupViews() {

    }

    override fun setupFlows() {

    }

    override fun setupListeners() {

    }

    override fun setupAdapters() {}
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }
}