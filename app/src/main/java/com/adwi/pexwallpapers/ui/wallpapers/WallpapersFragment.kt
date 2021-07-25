package com.adwi.pexwallpapers.ui.wallpapers

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adwi.pexwallpapers.base.BaseFragment
import com.adwi.pexwallpapers.databinding.FragmentWallpapersBinding
import com.adwi.pexwallpapers.shared.WallpaperListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class WallpapersFragment : BaseFragment<FragmentWallpapersBinding>() {

    override val viewModel: WallpaperViewModel by viewModels()
    override val binding: FragmentWallpapersBinding by viewBinding(CreateMethod.INFLATE)

    override fun setupViews() {
        val wallpaperListAdapter = WallpaperListAdapter()

        binding.apply {
            recyclerView.apply {
                adapter = wallpaperListAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.wallpaperList.collect { wallpapers ->
                    wallpaperListAdapter.submitList(wallpapers)
                }
            }
        }
    }
}