package com.adwi.pexwallpapers.ui.preview

import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.shared.tools.WallpaperSetter
import com.adwi.pexwallpapers.util.*
import com.adwi.pexwallpapers.util.Constants.Companion.WALLPAPER_ID
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PreviewFragment :
    BaseFragment<FragmentPreviewBinding, Any>(
        FragmentPreviewBinding::inflate,
        hasBackButton = true,
        hasOptionsMenu = true,
        hasNavigation = false
    ) {

    override val viewModel: PreviewViewModel by viewModels()

    private lateinit var wallpaper: Wallpaper

    private var doubleClick = false

    override fun setupToolbar() {
        binding.toolbar.apply {
            titleTextView.isVisible = false
            backButton.isVisible = true
        }
    }

    override fun setupViews() {
        val wallpaperId = arguments?.getInt(WALLPAPER_ID)
        if (wallpaperId != null) {
            binding.apply {
                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    viewModel.getWallpaperById(wallpaperId).collect {
                        val wallpaperFlow = it ?: return@collect
                        wallpaper = wallpaperFlow

                        wallpaperImageView.loadImageFromUrl(wallpaperFlow.imageUrl)
                        wallpaperImageView.byPhotographerContentDescription(wallpaperFlow.photographer)
                        wallpaperPhotographerTextView.byPhotographer(wallpaperFlow.photographer)

                        setWallpaperButton.setOnClickListener {
                            showDialog(wallpaperFlow.imageUrl)

                        }
                    }
                }

                wallpaperImageView.setOnClickListener {
                    doubleClick = true
                    if (doubleClick) {
                        wallpaperImageView.isClickable = false
                        heartImageView.visibility = View.VISIBLE
                        heartImageView.playAnimation()
                        favoriteOnDoubleClicked(wallpaper)
                    }

                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(2000)
                        !doubleClick
                        heartImageView.visibility = View.GONE
                        wallpaperImageView.isClickable = true
                    }
                }
            }
        }
    }

    override fun setupAdapter() {}

    private fun favoriteOnDoubleClicked(wallpaper: Wallpaper) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.favoriteOnDoubleClicked(wallpaper)
        }
    }

    private fun showDialog(imageUrl: String) {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.wallpaper_dialog_chooser)

        val home = dialog.findViewById<Button>(R.id.home_screen_button)
        val lock = dialog.findViewById<Button>(R.id.lock_screen_button)
        val homeAndLock = dialog.findViewById<Button>(R.id.home_and_lock_screen_button)

        home.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                WallpaperSetter(requireContext(), imageUrl).setWallpaperByImagePath(true)
            }
            dialog.dismiss()
        }

        lock.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                WallpaperSetter(
                    requireContext(),
                    imageUrl
                ).setWallpaperByImagePath(setLockScreen = true)
            }
            dialog.dismiss()
        }

        homeAndLock.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                WallpaperSetter(requireContext(), imageUrl).setWallpaperByImagePath(
                    setHomeScreen = true,
                    setLockScreen = true
                )
            }
            dialog.dismiss()
        }

        dialog.show()
    }
}