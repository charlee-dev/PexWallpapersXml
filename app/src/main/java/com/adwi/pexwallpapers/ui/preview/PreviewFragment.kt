package com.adwi.pexwallpapers.ui.preview

import android.app.WallpaperManager
import android.widget.ImageView
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import com.adwi.pexwallpapers.shared.base.BaseFragment
import com.adwi.pexwallpapers.tools.WallpaperSetter
import com.adwi.pexwallpapers.util.slideUp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PreviewFragment :
    BaseFragment<FragmentPreviewBinding>(FragmentPreviewBinding::inflate, true) {

    override val viewModel: PreviewViewModel by viewModels()
    private val args: PreviewFragmentArgs by navArgs()

    override fun setupViews() {

        binding.apply {
            wallpaper = args.wallpaper

            setWallpaperButton.setOnClickListener {
//                setWallpaper(binding.wallpaperImageView)
                viewLifecycleOwner.lifecycleScope.launch {
                    args.wallpaper.src?.portrait?.let { url ->
                        WallpaperSetter(requireContext()).setWallpaperByImagePath(
                            requireActivity(),
                            url
                        )
                    }
                }
            }
        }
    }

    private fun setWallpaper(imageView: ImageView) {
        val bitmap = binding.wallpaperImageView.drawToBitmap()
        val wallpaperManager = WallpaperManager.getInstance(requireContext())
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            wallpaperManager.setBitmap(bitmap)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNav.slideUp()
    }
}