package com.adwi.pexwallpapers.ui.preview

import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.shared.tools.WallpaperSetter
import com.adwi.pexwallpapers.util.launchCoroutine
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class PreviewFragment :
    BaseFragment<FragmentPreviewBinding, Any>(
        FragmentPreviewBinding::inflate,
        hasNavigation = false
    ) {

    override val viewModel: PreviewViewModel by viewModels()

    private lateinit var wallpaperArgs: Wallpaper

    private var doubleClickCounter = 0
    private val args: PreviewFragmentArgs by navArgs()

    override fun setupToolbar() {}

    override fun setupViews() {
        wallpaperArgs = args.wallpaper
        binding.apply {
            wallpaper = args.wallpaper
            executePendingBindings()
        }
    }

    override fun setupAdapters() {}
    override fun setupFlows() {}

    override fun setupListeners() {
        binding.apply {
            setWallpaperButton.setOnClickListener {
                showDialog(wallpaperArgs.imageUrl)
            }
            wallpaperImageView.setOnClickListener {
                doubleClickCounter++
                Timber.tag(TAG).d { "clicked: $doubleClickCounter" }
                if (doubleClickCounter > 1) {
                    wallpaperImageView.isClickable = false
                    doubleClickCounter = 0
                    heartImageView.visibility = View.VISIBLE
                    heartImageView.playAnimation()
                    favoriteOnDoubleClicked(wallpaperArgs)
                }
                launchCoroutine {
                    delay(1000)
                    doubleClickCounter = 0
                    heartImageView.visibility = View.GONE
                    wallpaperImageView.isClickable = true
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    private fun favoriteOnDoubleClicked(wallpaper: Wallpaper) {
        launchCoroutine {
            viewModel.favoriteOnDoubleClicked(wallpaper)
        }
    }

    private fun showDialog(imageUrl: String) {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.layoyt_wallpaper_dialog_chooser)

        val home = dialog.findViewById<Button>(R.id.home_screen_button)
        val lock = dialog.findViewById<Button>(R.id.lock_screen_button)
        val homeAndLock = dialog.findViewById<Button>(R.id.home_and_lock_screen_button)

        home.setOnClickListener {
            launchCoroutine {
                WallpaperSetter(requireContext(), imageUrl).setWallpaperByImagePath(true)
            }
            dialog.dismiss()
        }

        lock.setOnClickListener {
            launchCoroutine {
                WallpaperSetter(
                    requireContext(),
                    imageUrl
                ).setWallpaperByImagePath(setLockScreen = true)
            }
            dialog.dismiss()
        }

        homeAndLock.setOnClickListener {
            launchCoroutine {
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