package com.multibutton.library

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SpeedDialAction(context: Context) : LinearLayout(context) {

    val fab: FloatingActionButton

    private val cardView: MaterialCardView

    private val textView: TextView

    val label: String
        get() = textView.text.toString()

    init {
        with(View.inflate(context,
            FLOATING_ACTION_LAYOUT, this)) {
            layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.END
                setMargins(context.resources.getDimensionPixelSize(HORIZONTAL_MARGIN), 0,
                    context.resources.getDimensionPixelSize(HORIZONTAL_MARGIN), 0)
            }
            cardView = findViewById<MaterialCardView>(CARD_VIEW).apply {
                visibility = View.GONE
            }
            textView = findViewById<TextView>(TEXT_VIEW).apply {
                visibility = View.GONE
            }
            fab = findViewById(BUTTON)
            visibility = View.GONE
        }
    }


    /**
     *
     */
    internal fun show(buttonAnimation: Animation?, cardAnimation: Animation?) {
        super.setVisibility(View.VISIBLE)
        buttonAnimation?.let { fab.startAnimation(it) }
        cardAnimation?.let { cardView.startAnimation(it) }
    }

    /**
     *
     */
    internal fun hide(buttonAnimation: Animation?, cardAnimation: Animation?) {
        // TODO create Animation.AnimationListener to help declare when view is gone.
        buttonAnimation?.let { fab.startAnimation(it) }
        cardAnimation?.let { cardView.startAnimation(it) }
        super.setVisibility(View.GONE)
    }

    class Builder(private val context: Context) {

        @IdRes var id: Int? = null
        @DrawableRes var imageId: Int? = null
        @ColorRes var backgroundColor: Int? = null
        @ColorRes var textColor: Int? = null
        @ColorRes var textBackgroundColor: Int? = null
        var text: String? = null
        var listener: OnClickListener? = null

        fun setId(@IdRes id: Int) = apply {
            this.id = id
        }

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

        fun build(): SpeedDialAction {
            return SpeedDialAction(context).apply {
                fab.id = id

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

                listener?.let {
                    setOnClickListener(it)
                }
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