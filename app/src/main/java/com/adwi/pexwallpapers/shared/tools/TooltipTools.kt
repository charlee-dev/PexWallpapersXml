//package com.adwi.pexwallpapers.shared.tools
//
//import android.content.Context
//import android.view.View
//import android.widget.ImageView
//import com.adwi.pexwallpapers.R
//import com.adwi.pexwallpapers.data.local.entity.Wallpaper
//import com.adwi.pexwallpapers.util.isFavorite
//import com.adwi.pexwallpapers.util.showToast
//import com.skydoves.balloon.*
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//// Source https://github.com/skydoves/Balloon
//
//object TooltipTools {
//
//    fun createBalloonTooltip(
//        view: View,
//        context: Context,
//        favoriteAction: () -> Unit,
//        wallpaper: Wallpaper
//    ) {
//        // Builder
//        val balloon = createBalloon(context) {
//            setLayout(R.layout.buttons_container)
//            setArrowSize(15)
//            setWidth(BalloonSizeSpec.WRAP)
//            setHeight(BalloonSizeSpec.WRAP)
//            setArrowPosition(0.7f)
//            setArrowOrientation(ArrowOrientation.BOTTOM)
//            setCornerRadius(4f)
//            setAlpha(0.9f)
//            setPaddingBottom(10)
//            setBackgroundColorResource(R.color.white)
//            onBalloonClickListener?.let { setOnBalloonClickListener(it) }
//            setBalloonAnimation(BalloonAnimation.FADE)
//            setLifecycleOwner(lifecycleOwner)
//        }
//
//        // Views
//
//        val pexelsButton: ImageView =
//            balloon.getContentView().findViewById(R.id.pexels_button_bottom_sheet)
//        val shareButton: ImageView =
//            balloon.getContentView().findViewById(R.id.share_button_bottom_sheet)
//        val downloadButton: ImageView =
//            balloon.getContentView().findViewById(R.id.download_button_bottom_sheet)
//        val favoriteButton: ImageView =
//            balloon.getContentView().findViewById(R.id.favorites_bookmark_bottom_sheet)
//
//        favoriteButton.isFavorite(wallpaper.isFavorite)
//
//        // Listeners
//        pexelsButton.setOnClickListener {
//            UrlTools(context).openUrlInBrowser(wallpaper.url!!)
//            balloon.dismiss()
//        }
//        shareButton.setOnClickListener {
//            GlobalScope.launch(Dispatchers.IO) {
//                SharingTools(context).share(wallpaper.imageUrl, wallpaper.photographer)
//            }
//            balloon.dismiss()
//        }
//        downloadButton.setOnClickListener {
//            GlobalScope.launch(Dispatchers.IO) {
//                SharingTools(context).saveImageLocally(
//                    wallpaper.src!!.portrait,
//                    wallpaper.photographer
//                )
//            }
//            showToast(context, "Saved to Gallery - Photo by ${wallpaper.photographer}")
//            balloon.dismiss()
//        }
//        favoriteButton.setOnClickListener {
//            favoriteAction()
//            favoriteButton.isFavorite(wallpaper.isFavorite)
//            favoriteButton.invalidate()
//            balloon.dismiss()
//        }
//
//        balloon.dismissWithDelay(5000)
//        balloon.show(view)
//    }
//}
