package com.lovepreet.gallery.extensions

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import android.widget.TextView

/**
 * Created by lovepreetsingh on 10/09/22
 */

fun Activity.showSnackbar(
    message: String,
    actionButtonText: String? = "Okay",
    anchorView: View? = null,
    snackBarLength: Int? = null,
    completionHandler: ((value: Boolean) -> Unit)? = null){

    val snackbar: Snackbar = Snackbar.make(
        findViewById(android.R.id.content),
        message,
        snackBarLength ?: Snackbar.LENGTH_LONG).apply {
        this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.apply {
            maxLines = 5
            isSingleLine = false
        }
    }
    anchorView?.let{
        //eg: anchor to fab
        snackbar.setAnchorView(it)
    }

    val view = snackbar.view
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    view.layoutParams = params

    snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE

    actionButtonText?.let {
        snackbar.setAction(it) {
            snackbar.dismiss()
            completionHandler?.invoke(true)
        }
    }
    snackbar.show()
}