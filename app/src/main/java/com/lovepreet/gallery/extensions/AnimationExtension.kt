package com.lovepreet.gallery.extensions

import android.app.Activity
import com.lovepreet.gallery.R

fun Activity.fadeTransition(){
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
}