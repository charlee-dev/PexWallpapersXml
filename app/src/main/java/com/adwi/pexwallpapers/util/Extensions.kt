package com.adwi.pexwallpapers.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import coil.load
import com.adwi.pexwallpapers.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

val <T> T.exhaustive: T
    get() = this

fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()

// Views --------------------------------------------------------------------------------

fun View.fadeIn() {
    this.visibility = View.VISIBLE
    this.alpha = 0f
    this.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeIn.alpha = 1f
        }
    })
}

fun View.fadeOut() {
    this.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeOut.alpha = 1f
            this@fadeOut.visibility = View.GONE
        }
    })
}

fun ImageView.loadImageFromUrl(imageUrl: String) {
    this.load(imageUrl) {
        placeholder(BindingAdapters.shimmerDrawable)
        placeholder(R.drawable.placeholder_item)
        crossfade(600)
    }
}

fun ImageView.isFavorite(isFavorite: Boolean) {
    if (isFavorite) {
        this.setImageResource(R.drawable.ic_favorite_checked)
        Timber.d("checked")
    } else {
        this.setImageResource(R.drawable.ic_favorite_unchecked)
        Timber.d("unchecked")
    }
}

inline fun <T : View> T.showIfOrVisible(condition: (T) -> Boolean) {
    if (condition(this)) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

@SuppressLint("RestrictedApi")
fun Menu.showIcons() {
    (this as? MenuBuilder)?.setOptionalIconsVisible(true)
}

// Context --------------------------------------------------------------------------------

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

// Fragment --------------------------------------------------------------------------------

fun Fragment.launchCoroutine(body: suspend () -> Unit): Job {
    return viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        body()
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.showSnackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_LONG,
    view: View = requireView()
) {
    Snackbar.make(view, message, duration).show()
}

// ViewModel --------------------------------------------------------------------------------

fun ViewModel.onIO(body: suspend () -> Unit): Job {
    return viewModelScope.launch(Dispatchers.IO) {
        body()
    }
}