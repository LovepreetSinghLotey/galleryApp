package com.lovepreet.gallery.ui.customUi.readMoreTextView

import android.annotation.SuppressLint
import android.content.Context
import kotlin.jvm.JvmOverloads
import android.widget.TextView
import android.widget.TextView.BufferType
import com.lovepreet.gallery.ui.customUi.readMoreTextView.ReadMoreTextView.ReadMoreClickableSpan
import android.text.method.LinkMovementMethod
import com.lovepreet.gallery.ui.customUi.readMoreTextView.ReadMoreTextView
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.TextPaint
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.ViewTreeObserver
import android.os.Build
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.lovepreet.gallery.R
import androidx.core.content.ContextCompat
import java.lang.Exception

/**
 * Created by Lovepreet Singh on 11/09/22.
 */
@SuppressLint("AppCompatCustomView")
class ReadMoreTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    TextView(context, attrs) {

    private var readMoreText: CharSequence? = null
    private var bufferType: BufferType? = null
    private var readMore = true
    private var trimLength: Int
    private var trimCollapsedText: CharSequence
    private var trimExpandedText: CharSequence
    private val viewMoreSpan: ReadMoreClickableSpan
    private var colorClickableText: Int
    private val showTrimExpandedText: Boolean
    private var trimMode: Int
    private var lineEndIndex = 0
    private var trimLines: Int
    private fun setText() {
        super.setText(displayableText, bufferType)
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }

    private val displayableText: CharSequence?
        get() = getTrimmedText(readMoreText)

    override fun setText(readMoreText: CharSequence, type: BufferType) {
        this.readMoreText = readMoreText
        bufferType = type
        setText()
    }

    private fun getTrimmedText(readMoreText: CharSequence?): CharSequence? {
        if (trimMode == TRIM_MODE_LENGTH) {
            if (readMoreText != null && readMoreText.length > trimLength) {
                return if (readMore) {
                    updateCollapsedText()
                } else {
                    updateExpandedText()
                }
            }
        }
        if (trimMode == TRIM_MODE_LINES) {
            if (readMoreText != null && lineEndIndex > 0) {
                if (readMore) {
                    if (layout.lineCount > trimLines) {
                        return updateCollapsedText()
                    }
                } else {
                    return updateExpandedText()
                }
            }
        }
        return readMoreText
    }

    private fun updateCollapsedText(): CharSequence {
        var trimEndIndex = readMoreText!!.length
        when (trimMode) {
            TRIM_MODE_LINES -> {
                trimEndIndex = lineEndIndex - (ELLIPSIZE.length + trimCollapsedText.length + 1)
                if (trimEndIndex < 0) {
                    trimEndIndex = trimLength + 1
                }
            }
            TRIM_MODE_LENGTH -> trimEndIndex = trimLength + 1
        }
        val s = SpannableStringBuilder(readMoreText, 0, trimEndIndex)
            .append(ELLIPSIZE)
            .append(trimCollapsedText)
        return addClickableSpan(s, trimCollapsedText)
    }

    private fun updateExpandedText(): CharSequence? {
        if (showTrimExpandedText) {
            val s = SpannableStringBuilder(readMoreText, 0, readMoreText!!.length).append(trimExpandedText)
            return addClickableSpan(s, trimExpandedText)
        }
        return readMoreText
    }

    private fun addClickableSpan(s: SpannableStringBuilder, trimText: CharSequence): CharSequence {
        s.setSpan(
            viewMoreSpan,
            s.length - trimText.length,
            s.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return s
    }

    fun setTrimLength(trimLength: Int) {
        this.trimLength = trimLength
        setText()
    }

    fun setColorClickableText(colorClickableText: Int) {
        this.colorClickableText = colorClickableText
    }

    fun setTrimCollapsedText(trimCollapsedText: CharSequence) {
        this.trimCollapsedText = trimCollapsedText
    }

    fun setTrimExpandedText(trimExpandedText: CharSequence) {
        this.trimExpandedText = trimExpandedText
    }

    fun setTrimMode(trimMode: Int) {
        this.trimMode = trimMode
    }

    fun setTrimLines(trimLines: Int) {
        this.trimLines = trimLines
    }

    private inner class ReadMoreClickableSpan : ClickableSpan() {
        override fun onClick(widget: View) {
            readMore = !readMore
            setText()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = colorClickableText
        }
    }

    private fun onGlobalLayoutLineEndIndex() {
        if (trimMode == TRIM_MODE_LINES) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val obs = viewTreeObserver
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        obs.removeOnGlobalLayoutListener(this)
                    } else {
                        obs.removeGlobalOnLayoutListener(this)
                    }
                    refreshLineEndIndex()
                    setText()
                }
            })
        }
    }

    private fun refreshLineEndIndex() {
        try {
            lineEndIndex = if (trimLines == 0) {
                layout.getLineEnd(0)
            } else if (trimLines > 0 && lineCount >= trimLines) {
                layout.getLineEnd(trimLines - 1)
            } else {
                INVALID_END_INDEX
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TRIM_MODE_LINES = 0
        private const val TRIM_MODE_LENGTH = 1
        private const val DEFAULT_TRIM_LENGTH = 240
        private const val DEFAULT_TRIM_LINES = 2
        private const val INVALID_END_INDEX = -1
        private const val DEFAULT_SHOW_TRIM_EXPANDED_TEXT = true
        private const val ELLIPSIZE = "... "
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView)
        trimLength = typedArray.getInt(R.styleable.ReadMoreTextView_trimLength, DEFAULT_TRIM_LENGTH)
        trimCollapsedText = "Show more"
        trimExpandedText = "  Show less"
        trimLines = typedArray.getInt(R.styleable.ReadMoreTextView_trimLines, DEFAULT_TRIM_LINES)
        colorClickableText = typedArray.getColor(
            R.styleable.ReadMoreTextView_colorClickableText,
            ContextCompat.getColor(context, R.color.mdtp_accent_color_focused)
        )
        showTrimExpandedText = typedArray.getBoolean(
            R.styleable.ReadMoreTextView_showTrimExpandedText,
            DEFAULT_SHOW_TRIM_EXPANDED_TEXT
        )
        trimMode = typedArray.getInt(R.styleable.ReadMoreTextView_trimMode, TRIM_MODE_LINES)
        typedArray.recycle()
        viewMoreSpan = ReadMoreClickableSpan()
        onGlobalLayoutLineEndIndex()
        setText()
    }
}