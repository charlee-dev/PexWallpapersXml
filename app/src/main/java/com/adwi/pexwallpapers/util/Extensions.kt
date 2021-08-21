package com.adwi.pexwallpapers.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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

fun View.slideUp(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.slideDown(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
    visibility = View.GONE
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

fun ImageView.byPhotographerContentDescription(photographer: String) {
    val byText = "Picture taken by $photographer"
    this.contentDescription = byText
}

fun TextView.byPhotographer(photographer: String) {
    val byText = "by $photographer"
    this.text = byText
}

inline fun <T : View> T.showIfOrVisible(condition: (T) -> Boolean) {
    if (condition(this)) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

inline fun SearchView.onQueryTextSubmit(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (!query.isNullOrBlank()) {
                listener(query)
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    })
}

// Context --------------------------------------------------------------------------------

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

// Activity --------------------------------------------------------------------------------

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

// Fragment --------------------------------------------------------------------------------

fun Fragment.setTitle(title: String) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }
}

fun Fragment.setDisplayHomeAsUpEnabled(bool: Boolean) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            bool
        )
    }
}

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

fun ViewModel.onMain(body: suspend () -> Unit): Job {
    return viewModelScope.launch(Dispatchers.Main) {
        body()
    }
}