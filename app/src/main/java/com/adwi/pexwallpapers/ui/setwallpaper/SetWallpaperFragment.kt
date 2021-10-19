package com.adwi.pexwallpapers.ui.setwallpaper

import android.animation.ValueAnimator
import android.content.Intent
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.FragmentSetWallpaperBinding
import com.adwi.pexwallpapers.ui.base.BaseFragment
import com.adwi.pexwallpapers.util.CalendarUtil
import com.adwi.pexwallpapers.util.launchCoroutine
import com.adwi.pexwallpapers.util.showSnackbar
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
    private val currentHour = CalendarUtil().getCurrentHour()
    private val currentMinute = CalendarUtil().getCurrentMinutes()

    override fun setupViews() {
        wallpaperArgs = args.wallpaper
        homeLayerVisibility(true)
        binding.apply {
            wallpaper = wallpaperArgs
        }
    }

    override fun setupListeners() {
        binding.apply {
            setHomeButton.setOnClickListener {
                homeLayerVisibility(true)
            }
            setLockButton.setOnClickListener {
                homeLayerVisibility(false)
            }
            infoButton.setOnClickListener {
                findNavController().navigate(
                    SetWallpaperFragmentDirections.actionSetWallpaperFragmentToBottomSheetFragment(
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
                showDialog()
            }
            backButton.setOnClickListener {
                navigateBack()
            }
            pexelsButton.setOnClickListener {
                viewModel.goToPexels(wallpaperArgs)
            }
            shareButton.setOnClickListener {
                viewModel.shareWallpaper(requireContext(), wallpaperArgs)
            }
            downloadButton.setOnClickListener {
                viewModel.downloadWallpaper(wallpaperArgs)
                showSnackbar(
                    "Saved to Gallery - Photo by ${wallpaperArgs.photographer}",
                    view = root.rootView
                )
            }
            favoriteButton.setOnClickListener {
                launchCoroutine {
                    viewModel.onFavoriteClick(wallpaperArgs)
                    invalidateAll()
                }
            }
        }
    }

    private fun homeLayerVisibility(isVisible: Boolean) {
        binding.apply {
            if (isVisible) {
                sampleHeaderLayout.apply {
                    dateTextview.text = currentDate
                    dayOfWeekTextview.text = currentDayOfWeek
                }
            } else {
                sampleLockscreen.apply {
                    hoursTextview.text = currentHour
                    minutesTextview.text = currentMinute
                    dayOfWeekTextview.text = currentDayOfWeek
                    dateTextview.text = currentDate
                }
            }
            sampleHeaderLayout.sampleHeaderLayoutRoot.isVisible = isVisible
            sampleAppRowLayout.sampleAppRowLayoutRoot.isVisible = isVisible
            sampleSearchLayout.sampleSearchLayoutRoot.isVisible = isVisible
            sampleLockscreen.sampleLockscreenLayoutRoot.isVisible = !isVisible
        }
    }

    private fun showDialog() {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.set_wallpaper_dialog)
            .cornerRadius(res = R.dimen.image_corner_radius)

        val home = dialog.findViewById<Button>(R.id.set_home_button)
        val lock = dialog.findViewById<Button>(R.id.set_lock_button)
        val homeAndLock = dialog.findViewById<Button>(R.id.set_home_and_lock_button)

        home.setOnClickListener {
            viewModel.setWallpaper(
                wallpaperArgs.imageUrl, setHomeScreen = true, setLockScreen = false
            )
            showSnackbar(
                requireContext().getString(R.string.home_wallpaper_set),
                view = binding.root
            )
            dialog.dismiss()
        }
        lock.setOnClickListener {
            viewModel.setWallpaper(
                wallpaperArgs.imageUrl, setHomeScreen = false, setLockScreen = true
            )
            showSnackbar(
                requireContext().getString(R.string.lock_wallpaper_set),
                view = binding.root
            )
            dialog.dismiss()
        }
        homeAndLock.setOnClickListener {
            viewModel.setWallpaper(
                wallpaperArgs.imageUrl,
                setHomeScreen = true,
                setLockScreen = true
            )
            showSnackbar(
                requireContext().getString(R.string.home_and_lock_screen_wallpaper_set),
                view = binding.root
            )
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun setupToolbar() {}
    override fun setupAdapters() {}
    override fun setupFlows() {}
    override fun onMenuItemClick(item: MenuItem?) = false
}