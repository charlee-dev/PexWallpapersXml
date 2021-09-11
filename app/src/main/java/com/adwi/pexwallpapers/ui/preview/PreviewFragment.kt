package com.adwi.pexwallpapers.ui.preview

import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.adwi.pexwallpapers.databinding.FragmentPreviewBinding
import com.adwi.pexwallpapers.shared.adapter.WallpaperViewPager2Adapter
import com.adwi.pexwallpapers.ui.base.BaseFragment
import com.adwi.pexwallpapers.util.ZoomOutPageTransformer
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PreviewFragment :
    BaseFragment<FragmentPreviewBinding, WallpaperViewPager2Adapter>(
        FragmentPreviewBinding::inflate
    ) {

    override val viewModel: PreviewViewModel by viewModels()
    private val args: PreviewFragmentArgs by navArgs()

    override fun setupToolbar() {
        binding.toolbarContainer.apply {
            menuButton.isVisible = false
            backButton.isVisible = true
            titleTextView.apply {
                isVisible = true
                text = args.wallpaper.categoryName
            }
        }
    }

    override fun setupAdapters() {
        mAdapter = WallpaperViewPager2Adapter(
            onItemClick = { wallpaper ->
                findNavController().navigate(
                    PreviewFragmentDirections.actionPreviewFragmentToSetWallpaperFragment(
                        wallpaper
                    )
                )

            },
            onItemLongClick = { wallpaper ->
                viewModel.onFavoriteClick(wallpaper)
            }
        )
    }

    override fun setupViews() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            pager.apply {
                adapter = mAdapter
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                setPageTransformer(ZoomOutPageTransformer())
            }
            val list = args.wallaperLIst.toList()
            mAdapter?.submitList(list)
            val index = list.indexOf(args.wallpaper)
            pager.setCurrentItem(index, false)
        }
    }

    override fun setupListeners() {
        binding.apply {
            toolbarContainer.backButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun setupFlows() {}
    override fun onMenuItemClick(item: MenuItem?) = false
}