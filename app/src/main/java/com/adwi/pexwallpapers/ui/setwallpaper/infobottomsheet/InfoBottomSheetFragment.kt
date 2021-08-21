package com.adwi.pexwallpapers.ui.setwallpaper.infobottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adwi.pexwallpapers.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class InfoBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_bottom_sheet, container, false)
    }
}