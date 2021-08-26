package com.adwi.pexwallpapers.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.adwi.pexwallpapers.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}