package com.adwi.pexwallpapers.ui.settings

import android.view.MenuItem
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.databinding.FragmentSettingsBinding
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.ui.wallpapers.WallpaperViewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding, Any>(
    inflate = FragmentSettingsBinding::inflate
) {

    override val viewModel: WallpaperViewModel by viewModels()

    override fun setupToolbar() {
        binding.toolbarLayout.apply {
            menuButton.isVisible = false
            titleTextView.text = requireContext().getString(R.string.settings)
        }
    }

    override fun setupViews() {

    }

    override fun setupFlows() {

    }

    override fun setupListeners() {
        binding.apply {
            toolbarLayout.backButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun setupAdapters() {}
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }
}