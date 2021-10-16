package com.adwi.pexwallpapers.ui.settings

import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.data.local.entity.Settings
import com.adwi.pexwallpapers.databinding.FragmentSettingsBinding
import com.adwi.pexwallpapers.ui.base.BaseFragment
import com.adwi.pexwallpapers.util.launchCoroutine
import com.adwi.pexwallpapers.util.showSnackbar
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, Any>(
    inflate = FragmentSettingsBinding::inflate
) {
    override val viewModel: SettingsViewModel by viewModels()

    private lateinit var settings: Settings

    private var currentSliderValue = 5f

    override fun setupToolbar() {
        binding.toolbarLayout.apply {
            backButton.isVisible = true
            menuButton.isVisible = true
            titleTextView.text = requireContext().getString(R.string.settings)
        }
    }

    override fun setupViews() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            settingsViewModel = viewModel
        }
    }

    override fun setupListeners() {
        binding.apply {

            // Toolbar
            toolbarLayout.apply {
                backButton.setOnClickListener {
                    findNavController().popBackStack()
                }
                menuButton.setOnClickListener {
                    showMenu(it, R.menu.settings_menu)
                }
            }

            // Switches
            pushNotificationsSwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.updatePushNotification(checked)
            }
            newWallpaperSwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.updateNewWallpaperSet(checked)
            }
            wallpaperRecomendationsSwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.updateWallpaperRecommendations(checked)
            }
            autoWallpaperSwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.updateAutoChangeWallpaper(checked)
            }
            downloadOverWifiSwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.updateDownloadOverWiFi(checked)
            }

            // Radios
            changePeriodRadioGroup.setOnCheckedChangeListener { _, itemId ->
                viewModel.updateChangePeriodType(itemId)
                setSlider(itemId)
            }

            // Slider
            periodSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) {}
                override fun onStopTrackingTouch(slider: Slider) {
                    currentSliderValue = slider.value
                    viewModel.updateChangePeriodValue(slider.value)
                    periodDurationValue.text = slider.value.toInt().toString()
                }
            })

            // Buttons
            saveAutomationButton.setOnClickListener {
                Timber.tag(TAG).d("setupWorks")
                viewModel.setupWorks(requireContext(), settings)
                showSnackbar(getString(R.string.automation_settings_saved))
            }
            aboutButton.setOnClickListener { }
            supportButton.setOnClickListener { viewModel.contactSupport() }
            privacyPolicyButton.setOnClickListener { }
        }
    }

    override fun setupFlows() {
        binding.apply {
            launchCoroutine {
//                viewModel.changeWallpaperEvery.collect {
//                    setSlider(it)
//                }
                viewModel.settings.collect {
                    settings = it
                    pushNotificationsSwitch.isChecked = it.pushNotification
                    newWallpaperSwitch.isChecked = it.newWallpaperSet
                    wallpaperRecomendationsSwitch.isChecked = it.wallpaperRecommendations
                    autoWallpaperSwitch.isChecked = it.autoChangeWallpaper
                    changePeriodRadioGroup.check(it.selectedButton)
                    periodSlider.value = when (it.selectedButton) {
                        R.id.minutes_radio_button -> it.sliderMinutes
                        R.id.hours_radio_button -> it.sliderHours
                        else -> it.sliderDays
                    }
                    downloadOverWifiSwitch.isChecked = it.downloadOverWiFi

                    saveAutomationButton.isEnabled = settings.autoChangeWallpaper
                    saveAutomationButton.alpha = if (saveAutomationButton.isEnabled) 1f else .3f
                }
            }
        }
    }

    private fun setSlider(radioButton: Int) {
        binding.apply {
            var min = 1f
            val max: Float
            var step = 1f
            when (radioButton) {
                R.id.minutes_radio_button -> {
                    min = 15f
                    max = 60f
                    step = 5f
                }
                R.id.hours_radio_button -> max = 24f
                else -> max = 7f
            }
            sliderMinVlue.text = "$min"
            sliderMaxValue.text = "$max"
            periodSlider.apply {
                valueFrom = min
                valueTo = max
                stepSize = step
                value =
                    if (currentSliderValue > max || currentSliderValue < min || currentSliderValue % min != 0f)
                        max else currentSliderValue
            }
            periodDurationValue.text = periodSlider.value.toInt().toString()
            viewModel.updateChangePeriodValue(periodSlider.value)
        }
    }

    override fun setupAdapters() {}

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_reset_settings -> {
                viewModel.resetSettings()
                true
            }
            R.id.action_sign_out -> {
                true
            }
            else -> false
        }
    }
}