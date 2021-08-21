package com.adwi.pexwallpapers.ui.setwallpaper.infobottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.adwi.pexwallpapers.databinding.FragmentInfoBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class InfoBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentInfoBottomSheetBinding

    private val args: InfoBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInfoBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wallpaper = args.wallpaper
    }
}