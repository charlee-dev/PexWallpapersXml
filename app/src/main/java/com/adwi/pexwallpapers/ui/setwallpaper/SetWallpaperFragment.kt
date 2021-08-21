package com.adwi.pexwallpapers.ui.setwallpaper

import android.view.MenuItem
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.FragmentSetWallpaperBinding
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.shared.tools.WallpaperSetter
import com.adwi.pexwallpapers.util.CalendarUtil
import com.adwi.pexwallpapers.util.launchCoroutine
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetWallpaperFragment : BaseFragment<FragmentSetWallpaperBinding, Any>(
    inflate = FragmentSetWallpaperBinding::inflate
) {
    override val viewModel: SetWallpaperViewModel by viewModels()

    private lateinit var wallpaperArgs: Wallpaper

    private val args: SetWallpaperFragmentArgs by navArgs()
    private val currentDate = CalendarUtil().getTodayDate()
    private val currentDayOfWeek = "${CalendarUtil().getDayOfWeek()},"

    override fun setupViews() {
        wallpaperArgs = args.wallpaper
        binding.apply {
            wallpaper = wallpaperArgs
            sampleHeaderLayout.apply {
                dateTextview.text = currentDate
                dayOfWeekTextview.text = currentDayOfWeek
            }
        }
    }

    override fun setupFlows() {
        binding.apply {

        }
    }

    override fun setupListeners() {
        binding.apply {
            setHomeButton.setOnClickListener {
                launchCoroutine {
                    WallpaperSetter(
                        requireContext(),
                        wallpaperArgs.imageUrl
                    ).setWallpaperByImagePath(true)
                }
            }
            setLockButton.setOnClickListener {
                launchCoroutine {
                    WallpaperSetter(
                        requireContext(),
                        wallpaperArgs.imageUrl
                    ).setWallpaperByImagePath(setLockScreen = true)
                }
            }
            infoButton.setOnClickListener {
                findNavController().navigate(
                    SetWallpaperFragmentDirections.actionSetWallpaperFragmentToInfoBottomSheetFragment(
                        wallpaperArgs
                    )
                )
            }
            openInFullButton.setOnClickListener {

            }
            doneButton.setOnClickListener {
                showDialog(wallpaperArgs.imageUrl)
            }
            backButton.backImageView.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun setupToolbar() {}
    override fun setupAdapters() {}

    private fun showDialog(imageUrl: String) {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.wallpaper_dialog_chooser)

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

    override fun onMenuItemClick(item: MenuItem?) = false
}