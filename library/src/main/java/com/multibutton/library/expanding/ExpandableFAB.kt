package com.multibutton.library.expanding

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.annotation.StyleableRes
import com.multibutton.library.FAB
import com.multibutton.library.R
import kotlin.math.roundToInt

class ExpandableFAB : FAB {

    private val textView: TextView

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        with(context.obtainStyledAttributes(attrs, STYLEABLE_RES, 0, 0)) {
            textView = TextView(context).apply {
                id = FAB_TEXT_ID
                getText(TEXT_STYLEABLE_RES)?.let { strRes ->
                    text = strRes
                }
                val color = getColor(TEXT_COLOR_STYLEABLE_RES, View.NO_ID)
                if (color != View.NO_ID) {
                    setTextColor(color)
                }
                val size = getDimension(FONT_SIZE_STYLEABLE_RES, 0.0f)
                if (size > 0) {
                    textSize = size
                }

                layoutParams = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TEXT_DIMENSION, resources.displayMetrics).roundToInt(),
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TEXT_DIMENSION, resources.displayMetrics).roundToInt()
                )
            }
            addView(textView)
        }
    }

    fun setText(text: String) {
        textView.text = text
    }

    fun setTextColor(@ColorInt color: Int) {
        textView.setTextColor(color)
    }

    fun setTextColor(color: ColorStateList) {
        textView.setTextColor(color)
    }

    fun setFontSize(size: Float) {
        textView.textSize = size
    }

    fun setFontSize(unit: Int, size: Float) {
        textView.setTextSize(unit, size)
    }

    companion object {
        private const val TEXT_DIMENSION = 24.0f

        @IdRes private val FAB_TEXT_ID = R.id.fab_text

        /* Styleables Resources */
        @StyleableRes private val STYLEABLE_RES = R.styleable.ExpandableFAB
        @StyleableRes private val TEXT_STYLEABLE_RES = R.styleable.ExpandableFAB_text
        @StyleableRes private val TEXT_COLOR_STYLEABLE_RES = R.styleable.ExpandableFAB_textColor
        @StyleableRes private val FONT_SIZE_STYLEABLE_RES = R.styleable.ExpandableFAB_fontSize
    }
}