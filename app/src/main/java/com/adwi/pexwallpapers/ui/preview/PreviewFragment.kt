package com.adwi.pexwallpapers.ui.preview

import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import com.adwi.pexwallpapers.shared.adapter.WallpaperListAdapter
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.util.launchCoroutine
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class PreviewFragment :
    BaseFragment<FragmentPreviewBinding, WallpaperListAdapter>(
        FragmentPreviewBinding::inflate
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

    override fun setupListeners() {
        binding.apply {
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
            backButton.backImageView.setOnClickListener {
                findNavController().popBackStack()
            }
            infoContainer.setOnClickListener {
                findNavController().navigate(
                    PreviewFragmentDirections.actionPreviewFragmentToBottomSheetFragment(
                        wallpaperArgs
                    )
                )
            }
        }
    }

    override fun setupAdapters() {}
    override fun setupFlows() {}

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    private fun favoriteOnDoubleClicked(wallpaper: Wallpaper) {
        launchCoroutine {
            viewModel.favoriteOnDoubleClicked(wallpaper)
        }
    }
}