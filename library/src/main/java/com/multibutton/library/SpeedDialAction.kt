package com.multibutton.library

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SpeedDialAction private constructor(context: Context) : LinearLayout(context), Animation.AnimationListener {

    val fab: FloatingActionButton

    private val textView: TextView
    private val cardView: CardView

    private var showAnimation: Animation? = null
    private var hideAnimation: Animation? = null

    private var labelShowAnimation: Animation? = null
    private var labelHideAnimation: Animation? = null

    init {
        with(inflate(context, FLOATING_ACTION_LAYOUT, this)) {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.END
                setMargins(
                    context.resources.getDimensionPixelSize(HORIZONTAL_MARGIN), 0,
                    context.resources.getDimensionPixelSize(HORIZONTAL_MARGIN), 0
                )
            }

            cardView = findViewById(CARD_VIEW)
            textView = findViewById(TEXT_VIEW)
            fab = findViewById(BUTTON)
        }
        visibility = View.GONE
    }

    override fun onAnimationStart(animation: Animation?) {}

    override fun onAnimationEnd(animation: Animation?) {
        if (animation === hideAnimation) {
            visibility = View.GONE
        }
    }

    override fun onAnimationRepeat(animation: Animation?) {}

    internal fun show() {
        visibility = View.VISIBLE
        labelShowAnimation?.let { labelAnim ->
            cardView.startAnimation(labelAnim)
            showAnimation?.let { fabAnim ->
                fab.startAnimation(fabAnim)
            }
        } ?: showAnimation?.let {
            startAnimation(it)
        }
    }

    internal fun hide() {
        labelHideAnimation?.let { labelAnim ->
            cardView.startAnimation(labelAnim)
            hideAnimation?.let { fabAnim ->
                fab.startAnimation(fabAnim)
            }
        } ?: hideAnimation?.let {
            startAnimation(it)
        } ?: run {
            visibility = View.GONE
        }
    }

    class Builder(private val context: Context) {
        @DrawableRes private var imageId: Int? = null

        @ColorRes private var backgroundColor: Int? = null
        @ColorRes private var textColor: Int? = null
        @ColorRes private var textBackgroundColor: Int? = null

        private var text: String? = null
        private var listener: OnClickListener? = null
        private var showAnimation: Animation? = null
        private var hideAnimation: Animation? = null
        private var labelShowAnimation: Animation? = null
        private var labelHideAnimation: Animation? = null

        fun setImage(@DrawableRes imageId: Int) = apply {
            this.imageId = imageId
        }

        fun setBackgroundColor(@ColorRes color: Int) = apply {
            this.backgroundColor = color
        }

        fun setTextColor(@ColorRes color: Int) = apply {
            this.textColor = color
        }

        fun setTextBackgroundColor(@ColorRes color: Int) = apply {
            this.textBackgroundColor = color
        }

        fun setLabel(label: String) = apply {
            this.text = label
        }

        fun setOnClickListener(listener: OnClickListener) = apply {
            this.listener = listener
        }

        fun setShowAnimation(@AnimRes id: Int) = apply {
            showAnimation = AnimationUtils.loadAnimation(context, id)
        }

        fun setShowAnimation(animation: Animation) = apply {
            showAnimation = animation
        }

        fun setHideAnimation(@AnimRes id: Int) = apply {
            hideAnimation = AnimationUtils.loadAnimation(context, id)
        }

        fun setHideAnimation(animation: Animation) = apply {
            hideAnimation = animation
        }

        fun setLabelShowAnimation(@AnimRes id: Int) = apply {
            labelShowAnimation = AnimationUtils.loadAnimation(context, id)
        }

        fun setLabelShowAnimation(animation: Animation) = apply {
            labelShowAnimation = animation
        }

        fun setLabelHideAnimation(@AnimRes id: Int) = apply {
            labelHideAnimation = AnimationUtils.loadAnimation(context, id)
        }


        fun setLabelHideAnimation(animation: Animation) = apply {
            labelHideAnimation = animation
        }


        fun build(): SpeedDialAction = SpeedDialAction(context).apply {
            imageId?.let {
                fab.setImageDrawable(AppCompatResources.getDrawable(context, it))
            }

            backgroundColor?.let {
                fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, it))
            }

            text?.let {
                with(textView) {
                    text = it
                    visibility = android.view.View.VISIBLE
                    cardView.visibility = android.view.View.VISIBLE
                    textColor?.let {
                        textView.setTextColor(ContextCompat.getColor(context, it))
                    }
                    textBackgroundColor?.let {
                        cardView.setBackgroundColor(ContextCompat.getColor(context, it))
                    }
                }
            }

            this@Builder.showAnimation ?.let {
                showAnimation = it
            }

            this@Builder.hideAnimation ?.let {
                it.setAnimationListener(this)
                hideAnimation = it
            }

            this@Builder.labelShowAnimation?.let {
                labelShowAnimation = it
            }

            this@Builder.labelHideAnimation?.let {
                it.setAnimationListener(this)
                labelHideAnimation = it
            }

            listener?.let {
                setOnClickListener(it)
            }
        }
    }

    companion object {
        @LayoutRes private var FLOATING_ACTION_LAYOUT = R.layout.item_speed_dial

        @DimenRes private var HORIZONTAL_MARGIN = R.dimen.speed_dial_item_horizontal_margin

        @IdRes private var CARD_VIEW = R.id.item_card_view
        @IdRes private var TEXT_VIEW = R.id.item_text_view
        @IdRes private var BUTTON = R.id.item_button
    }
}