package com.adwi.pexwallpapers.ui.preview

import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import com.adwi.pexwallpapers.shared.adapter.WallpaperListAdapter
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.shared.tools.SharingTools
import com.adwi.pexwallpapers.util.launchCoroutine
import com.adwi.pexwallpapers.util.showSnackbar
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
                    doubleClickCounter = 0
                    wallpaperImageView.isClickable = false
                    heartImageView.visibility = View.VISIBLE
                    heartImageView.playAnimation()
                    favoriteOnDoubleClicked(wallpaperArgs)
                }
                launchCoroutine {
                    delay(1000)
                    doubleClickCounter = 0
                    heartImageView.visibility = View.INVISIBLE
                    wallpaperImageView.isClickable = true
                }
            }
            wallpaperImageView.setOnLongClickListener {
                viewModel.onFavoriteClick(wallpaperArgs)
                invalidateAll()
                true
            }
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
            setWallpaperButton.setOnClickListener {
                findNavController().navigate(
                    PreviewFragmentDirections.actionPreviewFragmentToSetWallpaperFragment(
                        wallpaperArgs
                    )
                )
            }
            pexelsButton.setOnClickListener {
                SharingTools(requireContext()).openUrlInBrowser(wallpaperArgs.url!!)
            }
            shareButton.setOnClickListener {
                launchCoroutine {
                    SharingTools(requireContext()).shareImage(
                        wallpaperArgs.imageUrl,
                        wallpaperArgs.photographer
                    )
                }
            }
            downloadButton.setOnClickListener {
                launchCoroutine {
                    SharingTools(requireContext()).saveImageLocally(
                        wallpaperArgs.imageUrl,
                        wallpaperArgs.photographer
                    )
                    showSnackbar(
                        "Saved to Gallery - Photo by ${wallpaperArgs.photographer}",
                        view = root.rootView
                    )
                }
            }
            favoriteButton.setOnClickListener {
                launchCoroutine {
                    viewModel.onFavoriteClick(wallpaperArgs)
                    invalidateAll()
                }
            }
        }
    }

    override fun setupToolbar() {}
    override fun setupAdapters() {}
    override fun setupFlows() {}
    override fun onMenuItemClick(item: MenuItem?) = false

    private fun favoriteOnDoubleClicked(wallpaper: Wallpaper) {
        launchCoroutine {
            viewModel.favoriteOnDoubleClicked(wallpaper)
            val img = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_checked)
            binding.apply {
                favoriteButton.setImageDrawable(img)
            }
        }
    }
}