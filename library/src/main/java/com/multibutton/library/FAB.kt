package com.multibutton.library

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StyleableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

open class FAB : FrameLayout {

    protected val button: Button

    protected val icon: ImageView

    protected val contentElevation: Float

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        with(context.obtainStyledAttributes(attrs, STYLEABLE_RES, 0, 0)) {
            val fabElevation: Float = getDimension(ELEVATION_STYLEABLE_RES, 0.0f)
            contentElevation = (fabElevation + 4) * 4.0f
            icon = ImageView(context).apply {
                id = FAB_ICON_ID
                scaleType = ImageView.ScaleType.FIT_XY
                elevation = contentElevation
                layoutParams = LayoutParams(
                    ICON_DIMENSION.toDipDimension(), // width
                    ICON_DIMENSION.toDipDimension()  // height
                ).apply {
                    gravity = Gravity.CENTER_VERTICAL
                    marginStart = 16.0f.toDipDimension()
                }
            }
            setIcon(getResourceId(ICON_STYLEABLE_RES, View.NO_ID))
            addView(icon)
            button = Button(context).apply {
                id = FAB_ID
                elevation = fabElevation
                background = ContextCompat.getDrawable(context, FAB_BACKGROUND_DRAWABLE)
                layoutParams = LayoutParams(
                    FAB_DIMENSION.toDipDimension(), // width
                    FAB_DIMENSION.toDipDimension()  // height
                )
            }
            addView(button)
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        super.setOnClickListener(listener)
        button.setOnClickListener(listener)
    }

    open fun setIcon(@DrawableRes iconRes: Int): Boolean = if (iconRes != View.NO_ID) {
        setIcon(AppCompatResources.getDrawable(context, iconRes))
    } else false

    open fun setIcon(iconDrawable: Drawable?): Boolean = iconDrawable?.let { drawable ->
        icon.setImageDrawable(drawable)
        true
    } ?: false

    /**
     * Convert float values to Device Independent Pixels (Dip/dp).
     * @return Returns Int as the measurement of the Dip dimension.
     */
    protected fun Float.toDipDimension(): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics).roundToInt()

    companion object {
        private const val FAB_DIMENSION = 56.0f
        private const val ICON_DIMENSION = 24.0f

        /* Identification Resources */
        @IdRes private val FAB_ID = R.id.fab_button
        @IdRes private val FAB_ICON_ID = R.id.fab_icon

        /* Styleables Resources */
        @StyleableRes private val STYLEABLE_RES = R.styleable.FAB
        @StyleableRes private val ICON_STYLEABLE_RES = R.styleable.FAB_icon
        @StyleableRes private val ELEVATION_STYLEABLE_RES = R.styleable.FAB_elevation_height

        /* Drawable Resources */
        @DrawableRes private val FAB_BACKGROUND_DRAWABLE = R.drawable.fab_background
    }
}