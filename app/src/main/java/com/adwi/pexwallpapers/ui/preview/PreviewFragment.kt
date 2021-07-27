package com.adwi.pexwallpapers.ui.preview

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import com.adwi.pexwallpapers.shared.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewFragment : BaseFragment<FragmentPreviewBinding, PreviewViewModel>() {

    override val viewModel: PreviewViewModel by viewModels()
    override val binding: FragmentPreviewBinding by viewBinding(CreateMethod.INFLATE)

    private val args: PreviewFragmentArgs by navArgs()

    override fun setupViews() {
        val wallpaperId = args.id
        binding.wallpaperIdTextview.text = wallpaperId.toString()
    }
}