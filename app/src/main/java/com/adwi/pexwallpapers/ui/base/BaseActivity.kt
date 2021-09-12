package com.adwi.pexwallpapers.ui.base

import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.adwi.pexwallpapers.R

open class BaseActivity : AppCompatActivity() {
    fun setStatusBarNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //status bar transparent
            window.statusBarColor = Color.TRANSPARENT
            //navigation bar white
            window.navigationBarColor = resources.getColor(R.color.white, theme)
            val decorView = getWindow().decorView
            var vis = decorView.systemUiVisibility
            vis = vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vis = vis or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
            decorView.systemUiVisibility = vis
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
    }

    fun getAppBarHeight(): Int {
        var height = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        height += if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
        val typedValue = TypedValue()
        height += if (theme.resolveAttribute(android.R.attr.windowTitleSize, typedValue, true))
            TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics) else 0
        return height
    }
}