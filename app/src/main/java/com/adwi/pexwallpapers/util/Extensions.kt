package com.adwi.pexwallpapers.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import coil.load
import com.adwi.pexwallpapers.R
import com.google.android.material.snackbar.Snackbar


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

fun ImageView.byPhotographerContentDescription(photographer: String) {
    val byText = "Picture taken by $photographer"
    this.contentDescription = byText
}

fun TextView.byPhotographer(photographer: String) {
    val byText = "by $photographer"
    this.text = byText
}

fun Fragment.showSnackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_LONG,
    view: View = requireView()
) {
    Snackbar.make(view, message, duration).show()
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

val <T> T.exhaustive: T
    get() = this

// Navigation extensions
fun <T> Fragment.replaceFragment(fragment: T) {
    val fragmentManager = parentFragmentManager
    val transaction = fragmentManager.beginTransaction()
    if (fragment is Fragment) {
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}

fun <T> Fragment.navigateToFragmentWithArgumentInt(name: String, value: Int, destination: T?) {
    val args = Bundle()
    args.putInt(name, value)
    if (destination is Fragment) {
        destination.arguments = args
        replaceFragment(destination)
    }
}