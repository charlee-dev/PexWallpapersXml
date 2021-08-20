package com.adwi.pexwallpapers.ui.preview.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.adwi.pexwallpapers.data.local.entity.Wallpaper
import com.adwi.pexwallpapers.databinding.FragmentBottomSheetBinding
import com.adwi.pexwallpapers.shared.adapter.WallpaperListAdapter
import com.adwi.pexwallpapers.shared.tools.SharingTools
import com.adwi.pexwallpapers.shared.tools.UrlTools
import com.adwi.pexwallpapers.util.launchCoroutine
import com.adwi.pexwallpapers.util.showToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var wallpaperArgs: Wallpaper

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var _mAdapter: WallpaperListAdapter? = null
    private val mAdapter get() = _mAdapter!!

    private val viewModel: BottomSheetViewModel by viewModels()
    private val args: BottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupViews()
        setupListeners()
        setupFlows()
    }

    private fun setupAdapters() {
        _mAdapter = WallpaperListAdapter(
            requireActivity = requireActivity(),
            onItemClick = { wallpaper ->
                showToast(requireContext(), wallpaper.photographer)
            },
            itemRandomHeight = false
        )
    }

    private fun setupViews() {
        binding.apply {
            wallpaperArgs = args.wallpaper
            wallpaper = wallpaperArgs
            executePendingBindings()

            recyclerView.apply {
                adapter = mAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            pexelsButton.setOnClickListener {
                UrlTools(requireContext()).openUrlInBrowser(wallpaperArgs.url!!)
            }
            shareButton.setOnClickListener {
                SharingTools(requireContext()).share(wallpaperArgs.imageUrl)
            }
            favoritesBookmark.setOnClickListener {
                viewModel.onFavoriteClick(wallpaperArgs)
            }
        }
    }

    private fun setupFlows() {
        launchCoroutine {
            val wallpapers = viewModel.getWallpapersOfCategory(wallpaperArgs.categoryName)
            mAdapter.submitList(wallpapers)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _mAdapter = null
    }
}