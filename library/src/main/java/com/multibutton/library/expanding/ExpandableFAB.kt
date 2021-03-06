package com.multibutton.library.expanding

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.annotation.StyleableRes
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.multibutton.library.FAB
import com.multibutton.library.R
import kotlin.math.roundToInt

class ExpandableFAB : FAB {

    private val textView: TextView

    /** State of the FAB. */
    var state: State = State.EXPANDED
        private set

    private val fabWidth: Int
        get() = icon.width + textView.width

    private val transition: Transition

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        with(context.obtainStyledAttributes(attrs, STYLEABLE_RES, 0, 0)) {
            textView = TextView(context).apply {
                id = FAB_TEXT_ID
                elevation = contentElevation
                getText(TEXT_STYLEABLE_RES)?.let { strRes ->
                    text = strRes
                }
                val color = getColor(TEXT_COLOR_STYLEABLE_RES, ATTRIBUTE_NOT_SET)
                if (color != ATTRIBUTE_NOT_SET) {
                    setTextColor(color)
                }
                textSize = TEXT_SIZE
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT, // width
                    LayoutParams.MATCH_PARENT  // height
                ).apply {
                    setPadding(
                        TEXT_PADDING_LEFT.toDipDimension(),
                        TEXT_PADDING_TOP.toDipDimension(),
                        TEXT_PADDING_RIGHT.toDipDimension(),
                        0)
                }
            }
            addView(textView)

            button.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                FAB_DIMENSION.toDipDimension()
            )
        }
        transition = TransitionInflater.from(context).inflateTransition(R.transition.expandable_fab_transition).apply {
            duration = 100
        }
    }

    /**
     * Change the state of the FAB. Changes from collapsed to expanded, vice versa.
     */
    fun changeState(animate: Boolean = true) {
        if (animate) {
            TransitionManager.beginDelayedTransition(parent as ViewGroup, transition)
        }
        state = state.change(resources, textView, button)
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

    enum class State {
        COLLAPSED {
            override fun change(
                resources: Resources,
                textView: TextView,
                button: Button
            ): State {
                textView.visibility = View.VISIBLE
                button.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FAB_DIMENSION, resources.displayMetrics).roundToInt()
                )
                return EXPANDED
            }
        },
        EXPANDED {
            override fun change(
                resources: Resources,
                textView: TextView,
                button: Button
            ): State {
                textView.visibility = View.GONE
                button.layoutParams = LayoutParams(
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FAB_DIMENSION, resources.displayMetrics).roundToInt(),
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FAB_DIMENSION, resources.displayMetrics).roundToInt()
                )
                return COLLAPSED
            }
        };

        internal abstract fun change(resources: Resources, textView: TextView, button: Button): State
    }

    companion object {
        private const val TEXT_SIZE = 16.0f

        private const val TEXT_PADDING_LEFT = 56.0f
        private const val TEXT_PADDING_RIGHT = 16.0f
        private const val TEXT_PADDING_TOP = 16.0f

        private const val FAB_DIMENSION = 56.0f

        private const val ATTRIBUTE_NOT_SET = Int.MIN_VALUE

        @IdRes private val FAB_TEXT_ID = R.id.fab_text

        /* Styleables Resources */
        @StyleableRes private val STYLEABLE_RES = R.styleable.ExpandableFAB
        @StyleableRes private val TEXT_STYLEABLE_RES = R.styleable.ExpandableFAB_text
        @StyleableRes private val TEXT_COLOR_STYLEABLE_RES = R.styleable.ExpandableFAB_textColor
        @StyleableRes private val FONT_SIZE_STYLEABLE_RES = R.styleable.ExpandableFAB_fontSize
    }
}