package com.adwi.pexwallpapers.ui.preview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.adwi.pexwallpapers.R
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewFragment : Fragment(R.layout.fragment_preview) {

    private val viewModel: PreviewViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPreviewBinding.bind(view)

    }
}