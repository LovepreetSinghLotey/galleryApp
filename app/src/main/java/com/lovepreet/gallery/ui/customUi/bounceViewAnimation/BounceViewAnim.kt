package com.lovepreet.gallery.ui.customUi.bounceViewAnimation

import android.view.animation.AccelerateDecelerateInterpolator

/**
 * Created by lovepreetsingh on 10/09/22
 */
interface BounceViewAnim {
    fun setScaleForPushInAnim(scaleX: Float, scaleY: Float): BounceViewAnim?
    fun setScaleForPopOutAnim(scaleX: Float, scaleY: Float): BounceViewAnim?
    fun setPushInAnimDuration(timeInMillis: Int): BounceViewAnim?
    fun setPopOutAnimDuration(timeInMillis: Int): BounceViewAnim?
    fun setInterpolatorPushIn(interpolatorPushIn: AccelerateDecelerateInterpolator?): BounceViewAnim?
    fun setInterpolatorPopOut(interpolatorPopOut: AccelerateDecelerateInterpolator?): BounceViewAnim?
}