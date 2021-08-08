package com.adwi.pexwallpapers.ui.preview

import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.shared.tools.WallpaperSetter
import com.adwi.pexwallpapers.util.Constants.Companion.WALLPAPER_ID
import com.adwi.pexwallpapers.util.byPhotographer
import com.adwi.pexwallpapers.util.byPhotographerContentDescription
import com.adwi.pexwallpapers.util.loadImageFromUrl
import com.adwi.pexwallpapers.util.slideUp
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class PreviewFragment :
    BaseFragment<FragmentPreviewBinding>(FragmentPreviewBinding::inflate, true) {

    override val viewModel: PreviewViewModel by viewModels()

    override fun setupViews() {

        val wallpaperId = arguments?.getInt(WALLPAPER_ID)

        if (wallpaperId != null) {
            binding.apply {
                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    viewModel.getWallpaperById(wallpaperId).collect {
                        val wallpaperFlow = it ?: return@collect

                        wallpaperImageView.loadImageFromUrl(wallpaperFlow.imageUrl)
                        wallpaperImageView.byPhotographerContentDescription(wallpaperFlow.photographer)
                        wallpaperPhotographerTextView.byPhotographer(wallpaperFlow.photographer)

                        setWallpaperButton.setOnClickListener {
                            showDialog(wallpaperFlow.imageUrl)

                        }
                    }
                }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNav.slideUp()
    }
}