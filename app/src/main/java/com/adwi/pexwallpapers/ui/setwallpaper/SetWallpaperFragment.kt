package com.adwi.pexwallpapers.ui.setwallpaper

import android.animation.ValueAnimator
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

    override fun setupListeners() {
        binding.apply {
            setHomeButton.setOnClickListener {
                setWallpaper(setHomeScreen = true, setLockScreen = false)
            }
            setLockButton.setOnClickListener {
                setWallpaper(setHomeScreen = false, setLockScreen = true)
            }
            infoButton.setOnClickListener {
                findNavController().navigate(
                    SetWallpaperFragmentDirections.actionSetWallpaperFragmentToInfoBottomSheetFragment(
                        wallpaperArgs
                    )
                )
            }
            openInFullButton.setOnClickListener {
                rootLayout.transitionToEnd()
                val animator = ValueAnimator.ofFloat(20f, 0f)
                animator.setDuration(1000)
                    .addUpdateListener { animation ->
                        val value = animation.animatedValue as Float
                        cardView.radius = value
                    }
                animator.start()
            }
            doneButton.setOnClickListener {
                showDialog(wallpaperArgs.imageUrl)
            }
            backButton.backImageView.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun showDialog(imageUrl: String) {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.set_wallpaper_dialog)
            .cornerRadius(res = R.dimen.image_corner_radius)

        val home = dialog.findViewById<Button>(R.id.set_home_button)
        val lock = dialog.findViewById<Button>(R.id.set_lock_button)
        val homeAndLock = dialog.findViewById<Button>(R.id.set_home_and_lock_button)

        home.setOnClickListener {
            setWallpaper(setHomeScreen = true, setLockScreen = false)
            dialog.dismiss()
        }
        lock.setOnClickListener {
            setWallpaper(setHomeScreen = false, setLockScreen = true)
            dialog.dismiss()
        }
        homeAndLock.setOnClickListener {
            setWallpaper(setHomeScreen = true, setLockScreen = true)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun setWallpaper(setHomeScreen: Boolean, setLockScreen: Boolean) {
        launchCoroutine {
            WallpaperSetter(
                requireContext(),
                wallpaperArgs.imageUrl
            ).setWallpaperByImagePath(
                setHomeScreen = setHomeScreen,
                setLockScreen = setLockScreen
            )
        }
    }

    override fun setupToolbar() {}
    override fun setupAdapters() {}
    override fun setupFlows() {}
    override fun onMenuItemClick(item: MenuItem?) = false
}