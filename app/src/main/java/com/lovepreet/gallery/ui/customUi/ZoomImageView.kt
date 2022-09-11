package com.lovepreet.gallery.ui.customUi

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.scaleMatrix
import androidx.lifecycle.MutableLiveData
import javax.annotation.Nullable

/**
 * Created by Lovepreet Singh on 11/09/22.
 */

class ZoomImageView: AppCompatImageView, View.OnTouchListener,
    GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    //shared constructing
    private var mContext: Context? = null
    private var mScaleDetector: ScaleGestureDetector? = null
    private var mGestureDetector: GestureDetector? = null
    var mMatrix: Matrix? = null
    private var mMatrixValues: FloatArray? = null
    var mode = NONE

    // Scales
    val imageZoomObserver: MutableLiveData<Float> = MutableLiveData<Float>().apply {
        this.value = 1f
    }

    var mMinScale = 1f
    var mMaxScale = 4f

    // view dimensions
    var origWidth = 0f
    var origHeight = 0f
    var viewWidth = 0
    var viewHeight = 0
    private var mLast = PointF()
    private var mStart = PointF()

    constructor(context: Context) : super(context) {
        sharedConstructing(context)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        sharedConstructing(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    private fun sharedConstructing(context: Context) {
        super.setClickable(true)
        mContext = context
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        mMatrix = Matrix()
        mMatrixValues = FloatArray(9)
        imageMatrix = mMatrix
        scaleType = ScaleType.MATRIX
        mGestureDetector = GestureDetector(context, this)
        setOnTouchListener(this)
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = ZOOM
            Log.d("scaling", "scale start $imageZoomObserver.value")
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            Log.d("scaling", "scale end $imageZoomObserver.value")
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var mScaleFactor = detector.scaleFactor
            val prevScale: Float = imageZoomObserver.value!!
            imageZoomObserver.value = imageZoomObserver.value!! * mScaleFactor
            if (imageZoomObserver.value!! > mMaxScale) {
                imageZoomObserver.value = mMaxScale
                mScaleFactor = mMaxScale / prevScale
            } else if (imageZoomObserver.value!! < mMinScale) {
                imageZoomObserver.value = mMinScale
                mScaleFactor = mMinScale / prevScale
            }
            if (origWidth * imageZoomObserver.value!! <= viewWidth
                || origHeight * imageZoomObserver.value!! <= viewHeight) {
                mMatrix!!.postScale(mScaleFactor, mScaleFactor, viewWidth / 2.toFloat(),
                    viewHeight / 2.toFloat())
            } else {
                mMatrix!!.postScale(mScaleFactor, mScaleFactor,
                    detector.focusX, detector.focusY)
            }
            fixTranslation()
            return true
        }
    }

    private  fun fitToScreen() {
        imageZoomObserver.value = 1f
        val scale: Float
        val drawable = drawable
        if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) return
        val imageWidth = drawable.intrinsicWidth
        val imageHeight = drawable.intrinsicHeight
        val scaleX = viewWidth.toFloat() / imageWidth.toFloat()
        val scaleY = viewHeight.toFloat() / imageHeight.toFloat()
        scale = scaleX.coerceAtMost(scaleY)
        mMatrix!!.setScale(scale, scale)

        // Center the image
        var redundantYSpace = (viewHeight.toFloat()
                - scale * imageHeight.toFloat())
        var redundantXSpace = (viewWidth.toFloat()
                - scale * imageWidth.toFloat())
        redundantYSpace /= 2.toFloat()
        redundantXSpace /= 2.toFloat()
        mMatrix!!.postTranslate(redundantXSpace, redundantYSpace)
        origWidth = viewWidth - 2 * redundantXSpace
        origHeight = viewHeight - 2 * redundantYSpace
        imageMatrix = mMatrix
    }

    private fun zoomLittle(){
        mode = ZOOM
        var mScaleFactor = 1.3f
        val prevScale: Float = imageZoomObserver.value!!
        imageZoomObserver.value = imageZoomObserver.value!! * mScaleFactor
        if (imageZoomObserver.value!! > mMaxScale) {
            imageZoomObserver.value = mMaxScale
            mScaleFactor = mMaxScale / prevScale
        } else if (imageZoomObserver.value!! < mMinScale) {
            imageZoomObserver.value = mMinScale
            mScaleFactor = mMinScale / prevScale
        }
        if (origWidth * imageZoomObserver.value!! <= viewWidth
            || origHeight * imageZoomObserver.value!! <= viewHeight) {
            mMatrix!!.postScale(mScaleFactor, mScaleFactor, viewWidth / 2.toFloat(),
                viewHeight / 2.toFloat())
        }
        fixTranslation()
    }

    fun fixTranslation() {
        mMatrix!!.getValues(mMatrixValues) //put matrix values into a float array so we can analyze
        val transX = mMatrixValues!![Matrix.MTRANS_X] //get the most recent translation in x direction
        val transY = mMatrixValues!![Matrix.MTRANS_Y] //get the most recent translation in y direction
        val fixTransX = getFixTranslation(transX, viewWidth.toFloat(), origWidth * imageZoomObserver.value!!)
        val fixTransY = getFixTranslation(transY, viewHeight.toFloat(), origHeight * imageZoomObserver.value!!)
        if (fixTransX != 0f || fixTransY != 0f) mMatrix!!.postTranslate(fixTransX, fixTransY)
    }

    private fun getFixTranslation(trans: Float, viewSize: Float, contentSize: Float): Float {
        val minTrans: Float
        val maxTrans: Float
        if (contentSize <= viewSize) { // case: NOT ZOOMED
            minTrans = 0f
            maxTrans = viewSize - contentSize
        } else { //CASE: ZOOMED
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }
        if (trans < minTrans) { // negative x or y translation (down or to the right)
            return -trans + minTrans
        }
        if (trans > maxTrans) { // positive x or y translation (up or to the left)
            return -trans + maxTrans
        }
        return 0F
    }

    private fun getFixDragTrans(delta: Float, viewSize: Float, contentSize: Float): Float {
        return if (contentSize <= viewSize) {
            0F
        } else delta
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (imageZoomObserver.value == 1f) {
            // Fit to screen.
            fitToScreen()
        }
    }

    /*
        Ontouch
     */
    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        mScaleDetector!!.onTouchEvent(event)
        mGestureDetector!!.onTouchEvent(event)
        val currentPoint = PointF(event.x, event.y)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLast.set(currentPoint)
                mStart.set(mLast)
                mode = DRAG
            }
            MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                val dx = currentPoint.x - mLast.x
                val dy = currentPoint.y - mLast.y
                val fixTransX = getFixDragTrans(dx, viewWidth.toFloat(), origWidth * imageZoomObserver.value!!)
                val fixTransY = getFixDragTrans(dy, viewHeight.toFloat(), origHeight * imageZoomObserver.value!!)
                mMatrix!!.postTranslate(fixTransX, fixTransY)
                fixTranslation()
                mLast[currentPoint.x] = currentPoint.y
            }
            MotionEvent.ACTION_POINTER_UP -> mode = NONE
        }
        imageMatrix = mMatrix
        return false
    }

    /*
        GestureListener
     */
    override fun onDown(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(motionEvent: MotionEvent) {}
    override fun onSingleTapUp(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(motionEvent: MotionEvent, motionEvent1: MotionEvent, v: Float, v1: Float): Boolean {
        return false
    }

    override fun onLongPress(motionEvent: MotionEvent) {}
    override fun onFling(motionEvent: MotionEvent, motionEvent1: MotionEvent, v: Float, v1: Float): Boolean {
        return false
    }

    /*
        onDoubleTap
     */
    override fun onSingleTapConfirmed(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onDoubleTap(motionEvent: MotionEvent): Boolean {
        if(imageZoomObserver.value == 1f){
            zoomLittle()
        } else {
            fitToScreen()
        }
        return false
    }

    override fun onDoubleTapEvent(motionEvent: MotionEvent): Boolean {
        return false
    }

    companion object {

        // Image States
        const val NONE = 0
        const val DRAG = 1
        const val ZOOM = 2
    }
}