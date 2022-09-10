package com.lovepreet.gallery.ui.customUi.bounceViewAnimation

import com.google.android.material.tabs.TabLayout
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.View.OnTouchListener
import android.view.MotionEvent
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import com.lovepreet.gallery.R
import android.view.ViewGroup
import android.widget.PopupWindow
import java.lang.ref.WeakReference

/**
 * Created by lovepreetsingh on 10/09/22
 */
class BounceView : BounceViewAnim {
    private var view: WeakReference<View?>? = null
    private var dialog: WeakReference<Dialog?>? = null
    private var popup: WeakReference<PopupWindow?>? = null
    private var tabLayout: WeakReference<TabLayout?>? = null
    private var isTouchInsideView = true
    private var pushInScaleX = PUSH_IN_SCALE_X
    private var pushInScaleY = PUSH_IN_SCALE_Y
    private var popOutScaleX = POP_OUT_SCALE_X
    private var popOutScaleY = POP_OUT_SCALE_Y
    private var pushInAnimDuration = PUSH_IN_ANIM_DURATION
    private var popOutAnimDuration = POP_OUT_ANIM_DURATION
    private var pushInInterpolator = DEFAULT_INTERPOLATOR
    private var popOutInterpolator = DEFAULT_INTERPOLATOR

    private constructor(view: View) {
        this.view = WeakReference(view)
        if (this.view!!.get() != null) {
            if (!this.view!!.get()!!.hasOnClickListeners()) {
                this.view!!.get()!!.setOnClickListener { }
            }
        }
    }

    private constructor(dialog: Dialog) {
        this.dialog = WeakReference(dialog)
    }

    private constructor(popup: PopupWindow) {
        this.popup = WeakReference(popup)
    }

    private constructor(tabLayout: TabLayout) {
        this.tabLayout = WeakReference(tabLayout)
    }

    override fun setScaleForPushInAnim(scaleX: Float, scaleY: Float): BounceViewAnim {
        pushInScaleX = scaleX
        pushInScaleY = scaleY
        return this
    }

    override fun setScaleForPopOutAnim(scaleX: Float, scaleY: Float): BounceViewAnim {
        popOutScaleX = scaleX
        popOutScaleY = scaleY
        return this
    }

    override fun setPushInAnimDuration(timeInMillis: Int): BounceViewAnim {
        pushInAnimDuration = timeInMillis
        return this
    }

    override fun setPopOutAnimDuration(timeInMillis: Int): BounceViewAnim {
        popOutAnimDuration = timeInMillis
        return this
    }

    override fun setInterpolatorPushIn(interpolatorPushIn: AccelerateDecelerateInterpolator?): BounceViewAnim? {
        interpolatorPushIn?.let { pushInInterpolator = it }
        return this
    }

    override fun setInterpolatorPopOut(interpolatorPopOut: AccelerateDecelerateInterpolator?): BounceViewAnim? {
        interpolatorPopOut?.let { popOutInterpolator = it }
        return this
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setAnimToView() {
        if (view != null) {
            view!!.get()!!.setOnTouchListener(OnTouchListener { v, motionEvent ->
                val action = motionEvent.action
                if (action == MotionEvent.ACTION_DOWN) {
                    isTouchInsideView = true
                    startAnimScale(
                        v,
                        pushInScaleX,
                        pushInScaleY,
                        pushInAnimDuration,
                        pushInInterpolator,
                        0
                    )
                } else if (action == MotionEvent.ACTION_UP) {
                    if (isTouchInsideView) {
                        v.animate().cancel()
                        startAnimScale(
                            v,
                            popOutScaleX,
                            popOutScaleY,
                            popOutAnimDuration,
                            popOutInterpolator,
                            0
                        )
                        startAnimScale(
                            v,
                            1f,
                            1f,
                            popOutAnimDuration,
                            popOutInterpolator,
                            popOutAnimDuration + 1
                        )
                        return@OnTouchListener false
                    }
                } else if (action == MotionEvent.ACTION_CANCEL) {
                    if (isTouchInsideView) {
                        v.animate().cancel()
                        startAnimScale(v, 1f, 1f, popOutAnimDuration, DEFAULT_INTERPOLATOR, 0)
                    }
                    return@OnTouchListener true
                } else if (action == MotionEvent.ACTION_MOVE) {
                    if (isTouchInsideView) {
                        val currentX = motionEvent.x
                        val currentY = motionEvent.y
                        val currentPosX = currentX + v.left
                        val currentPosY = currentY + v.top
                        val viewLeft = v.left.toFloat()
                        val viewTop = v.top.toFloat()
                        val viewRight = v.right.toFloat()
                        val viewBottom = v.bottom.toFloat()
                        if (!(currentPosX > viewLeft && currentPosX < viewRight && currentPosY > viewTop && currentPosY < viewBottom)) {
                            isTouchInsideView = false
                            v.animate().cancel()
                            startAnimScale(v, 1f, 1f, popOutAnimDuration, DEFAULT_INTERPOLATOR, 0)
                        }
                        return@OnTouchListener true
                    }
                }
                false
            })
        }
    }

    private fun startAnimScale(
        view: View, scaleX: Float, scaleY: Float,
        animDuration: Int,
        interpolator: AccelerateDecelerateInterpolator,
        startDelay: Int
    ) {
        val animX = ObjectAnimator.ofFloat(view, "scaleX", scaleX)
        val animY = ObjectAnimator.ofFloat(view, "scaleY", scaleY)
        val animatorSet = AnimatorSet()
        animX.duration = animDuration.toLong()
        animX.interpolator = interpolator
        animY.duration = animDuration.toLong()
        animY.interpolator = interpolator
        animatorSet.playTogether(animX, animY)
        animatorSet.startDelay = startDelay.toLong()
        animatorSet.start()
    }

    private fun setAnimToDialog() {
//        if (dialog!!.get() != null) {
//            val dialogWindow = dialog!!.get()!!.window
//            dialogWindow?.setWindowAnimations(R.style.CustomDialogAnimation)
//        }
    }

    private fun setAnimToPopup() {
        if (popup!!.get() != null) {
            popup!!.get()!!.animationStyle = R.style.CustomDialogAnimation
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setAnimToTabLayout() {
        if (tabLayout!!.get() != null) {
            for (i in 0 until tabLayout!!.get()!!.tabCount) {
                val tab = tabLayout!!.get()!!.getTabAt(i)
                val tabView = (tabLayout!!.get()!!.getChildAt(0) as ViewGroup).getChildAt(i)
                tabView.setOnTouchListener(OnTouchListener { v, motionEvent ->
                    val action = motionEvent.action
                    if (action == MotionEvent.ACTION_DOWN) {
                        isTouchInsideView = true
                        startAnimScale(
                            v,
                            pushInScaleX,
                            pushInScaleY,
                            pushInAnimDuration,
                            pushInInterpolator,
                            0
                        )
                        return@OnTouchListener true
                    } else if (action == MotionEvent.ACTION_UP) {
                        if (isTouchInsideView) {
                            v.animate().cancel()
                            startAnimScale(
                                v,
                                popOutScaleX,
                                popOutScaleY,
                                popOutAnimDuration,
                                popOutInterpolator,
                                0
                            )
                            startAnimScale(
                                v,
                                1f,
                                1f,
                                popOutAnimDuration,
                                popOutInterpolator,
                                popOutAnimDuration + 1
                            )
                            tab!!.select()
                            return@OnTouchListener false
                        }
                    } else if (action == MotionEvent.ACTION_CANCEL) {
                        if (isTouchInsideView) {
                            v.animate().cancel()
                            startAnimScale(v, 1f, 1f, popOutAnimDuration, DEFAULT_INTERPOLATOR, 0)
                        }
                        return@OnTouchListener true
                    } else if (action == MotionEvent.ACTION_MOVE) {
                        if (isTouchInsideView) {
                            val currentX = motionEvent.x
                            val currentY = motionEvent.y
                            val currentPosX = currentX + v.left
                            val currentPosY = currentY + v.top
                            val viewLeft = v.left.toFloat()
                            val viewTop = v.top.toFloat()
                            val viewRight = v.right.toFloat()
                            val viewBottom = v.bottom.toFloat()
                            if (!(currentPosX > viewLeft && currentPosX < viewRight && currentPosY > viewTop && currentPosY < viewBottom)) {
                                isTouchInsideView = false
                                v.animate().cancel()
                                startAnimScale(
                                    v,
                                    1f,
                                    1f,
                                    popOutAnimDuration,
                                    DEFAULT_INTERPOLATOR,
                                    0
                                )
                            }
                            return@OnTouchListener true
                        }
                    }
                    false
                })
            }
        }
    }

    companion object {
        const val PUSH_IN_SCALE_X = 0.9f
        const val PUSH_IN_SCALE_Y = 0.9f
        const val POP_OUT_SCALE_X = 1.1f
        const val POP_OUT_SCALE_Y = 1.1f
        const val PUSH_IN_ANIM_DURATION = 100
        const val POP_OUT_ANIM_DURATION = 100
        val DEFAULT_INTERPOLATOR = AccelerateDecelerateInterpolator()
        fun addAnimTo(view: View): BounceView {
            val bounceAnim = BounceView(view)
            bounceAnim.setAnimToView()
            return bounceAnim
        }

        fun addAnimTo(dialog: Dialog) {
            val bounceAnim = BounceView(dialog)
            bounceAnim.setAnimToDialog()
        }

        fun addAnimTo(popupWindow: PopupWindow) {
            val bounceAnim = BounceView(popupWindow)
            bounceAnim.setAnimToPopup()
        }

        fun addAnimTo(tabLayout: TabLayout): BounceView {
            val bounceAnim = BounceView(tabLayout)
            bounceAnim.setAnimToTabLayout()
            return bounceAnim
        }
    }
}