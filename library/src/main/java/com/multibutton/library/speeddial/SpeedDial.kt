package com.multibutton.library.speeddial

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.multibutton.library.R
import kotlin.math.roundToInt

class SpeedDial : LinearLayout, Animation.AnimationListener {

    private var state: State = State.COLLAPSED

    /** Main button for SpeedDial. */
    private val button: ImageButton

    /** TransitionDrawable for background overlay transition. */
    private var backgroundTransition: TransitionDrawable?

    private var collapseIcon: Drawable? = null
    private var expandIcon: Drawable? = null

    private var showAnimation: Animation? = null
    private var hideAnimation: Animation? = null

    private var collapseAnimation: Animation? = null
    private var expandAnimation: Animation? = null

    private val actions: MutableSet<SpeedDialAction> = mutableSetOf()

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        with(context.obtainStyledAttributes(attrs, STYLEABLE_RES, 0, 0)) {
            val overlayColor = getColor(
                STYLEABLE_OVERLAY_COLOR,
                ATTRIBUTE_NOT_SET
            )
            backgroundTransition = if (overlayColor != ATTRIBUTE_NOT_SET) {
                val colors: Array<ColorDrawable> = arrayOf(
                    ColorDrawable(ContextCompat.getColor(context, COLOR_COLLAPSED_BACKGROUND)),
                    ColorDrawable(overlayColor)
                )

                TransitionDrawable(colors).also { background = it }
            } else {
                val colors: Array<ColorDrawable> = arrayOf(
                    ColorDrawable(ContextCompat.getColor(context, COLOR_COLLAPSED_BACKGROUND)),
                    ColorDrawable(ContextCompat.getColor(context, COLOR_EXPANDED_BACKGROUND)))

                TransitionDrawable(colors).also { background = it }
            }

            val showAnimResId: Int = getResourceId(
                STYLEABLE_FAB_SHOW_ANIMATION,
                RESOURCE_ID_NULL
            )
            val hideAnimResId: Int = getResourceId(
                STYLEABLE_FAB_HIDE_ANIMATION,
                RESOURCE_ID_NULL
            )

            showAnimation = if (showAnimResId != RESOURCE_ID_NULL) AnimationUtils.loadAnimation(context, showAnimResId) else null
            showAnimation?.setAnimationListener(this@SpeedDial)

            hideAnimation = if (hideAnimResId != RESOURCE_ID_NULL) AnimationUtils.loadAnimation(context, hideAnimResId) else null
            hideAnimation?.setAnimationListener(this@SpeedDial)

            button = ImageButton(context).apply {
                id = FAB_ID
                isClickable = true
                isFocusable = true
                background = ContextCompat.getDrawable(context, BACKGROUND_DRAWABLE)

                scaleType =
                    getScaleType(
                        getInt(
                            STYLEABLE_SCALE_TYPE,
                            DEFAULT_SCALE_TYPE_VALUE
                        )
                    )

                val color = getColor(
                    STYLEABLE_FAB_COLOR,
                    ATTRIBUTE_NOT_SET
                )
                if (color != ATTRIBUTE_NOT_SET) {
                    backgroundTintList = ColorStateList.valueOf(color)
                }

                layoutParams = LayoutParams(
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FAB_DIMENSION, resources.displayMetrics).roundToInt(),
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FAB_DIMENSION, resources.displayMetrics).roundToInt()).apply {

                    gravity = Gravity.END

                    val metrics = context.resources.displayMetrics

                    val margin = getDimension(
                        STYLEABLE_FAB_MARGIN,
                        MARGIN_NOT_SET
                    )
                    val marginTop =  getDimension(STYLEABLE_FAB_MARGIN_TOP, margin)
                    val marginBottom = getDimension(STYLEABLE_FAB_MARGIN_BOTTOM, margin)
                    val marginStart = getDimension(STYLEABLE_FAB_MARGIN_START, margin)
                    val marginEnd = getDimension(STYLEABLE_FAB_MARGIN_END, margin)
                    setMargins(
                        ((TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FAB_HORIZONTAL_MARGIN, metrics) + marginStart).roundToInt()), // Start margin
                        ((TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FAB_VERTICAL_MARGIN, metrics) + marginTop).roundToInt()),     // Top margin
                        ((TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FAB_HORIZONTAL_MARGIN, metrics) + marginEnd).roundToInt()),   // End margin
                        ((TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FAB_VERTICAL_MARGIN, metrics) + marginBottom).roundToInt()))  // Bottom margin
                }

                setOnClickListener {
                    changeState()
                }
            }

            val collapseIconRes = getResourceId(STYLEABLE_FAB_COLLAPSED_ICON, View.NO_ID)
            val expandIconRes = getResourceId(STYLEABLE_FAB_EXPANDED_ICON, collapseIconRes)
            val collapseAnimRes = getResourceId(STYLEABLE_FAB_COLLAPSE_ANIMATION, View.NO_ID)
            val expandAnimRes = getResourceId(STYLEABLE_FAB_EXPAND_ANIMATION, View.NO_ID)
            setIcon(
                collapseIconRes,
                expandIconRes,
                if (collapseAnimRes != View.NO_ID) collapseAnimRes else null,
                if (expandAnimRes != View.NO_ID) expandAnimRes else null)

            addView(button)
        }
    }

    init {
        orientation = VERTICAL
        gravity = Gravity.BOTTOM
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean = performClick()

    override fun performClick(): Boolean {
        super.performClick()
        return if (state == State.EXPANDED) {
            changeState()
            true
        } else {
            false
        }
    }

    override fun onAnimationStart(animation: Animation?) {}

    override fun onAnimationEnd(animation: Animation?) {
        if (animation === hideAnimation) {
            visibility = View.GONE
        }
    }

    override fun onAnimationRepeat(animation: Animation?) {}



    fun show() {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
            showAnimation?.let {
                if (!it.hasStarted() || it.hasEnded()) {
                    startAnimation(it)
                }
            }
        }
    }

    fun setShowAnimation(animation: Animation, listener: Animation.AnimationListener? = null) {
        showAnimation = listener?.let {
            animation.apply {
                setAnimationListener(it)
            }
        } ?: animation
    }

    fun hide() {
        if (visibility == View.VISIBLE) {
            hideAnimation?.let {
                if (!it.hasStarted() || it.hasEnded()) {
                    startAnimation(it)
                }
            } ?: run {
                visibility = View.GONE
            }
        }
    }

    fun setHideAnimation(animation: Animation, listener: Animation.AnimationListener? = null) {
        hideAnimation = listener?.let {
            animation.apply {
                setAnimationListener(it)
            }
        } ?: animation.apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    if (animation === animation) {
                        with(this@SpeedDial) {
                            visibility = View.GONE
                        }
                    }
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
    }

    /**
     * Provide an action for the speed dial button to display. An action is represented on the screen as
     * FloatingActionButtons lined up vertically.
     * @see [SpeedDialAction]
     */
    fun addAction(action: SpeedDialAction): Boolean{
        if (!actions.contains(action)) {
            actions.add(action)
            removeView(button)
            addView(action)
            addView(button)
            return true
        }
        return false
    }


    fun removeAction(action: SpeedDialAction): Boolean {
        if (actions.contains(action)) {
            removeView(action)
            actions.remove(action)
            return true
        }
        return false
    }

    /**
     * # Setting Icons
     * Set an icon for the SpeedDial. A SpeedDial can have two icons (an icon when the button is
     * resting in the collapsed state, and an icon when the button is expanded).<br/>
     * **NOTE**: If SpeedDial is only provided with a single icon, the icon will take place for both
     * collapsed and expanded states.
     *
     * # Setting animations
     * A SpeedDial can also be provided animations during clicks. You can optionally provided an animation
     * for collapsing, expanding, or both.
     * @param collapseIconRes Resource ID for the icon in the collapsed state.
     * @param expandIconRes Resource ID for the icon in the expanded state.
     * @param collapseAnimRes Animation resource ID for collapsing the button.
     * @param expandAnimRes Animation resource ID for expanding the button.
     */
    fun setIcon(
        @DrawableRes collapseIconRes: Int,
        @DrawableRes expandIconRes: Int? = null,
        @AnimRes collapseAnimRes: Int? = null,
        @AnimRes expandAnimRes: Int? = null
    ) {
        AppCompatResources.getDrawable(context, collapseIconRes)?.let { collapseDrawable ->
            setIcon(collapseDrawable,
                expandIconRes?.run { AppCompatResources.getDrawable(context, this) },
                collapseAnimRes?.run { AnimationUtils.loadAnimation(context, this) },
                expandAnimRes?.run { AnimationUtils.loadAnimation(context, this) })
        }
    }

    /**
     * # Setting Icons
     * Set an icon for the SpeedDial. A SpeedDial can have two icons (an icon when the button is
     * resting in the collapsed state, and an icon when the button is expanded).<br/>
     * **NOTE**: If SpeedDial is only provided with a single icon, the icon will take place for both
     * collapsed and expanded states.
     *
     * # Setting animations
     * A SpeedDial can also be provided animations during clicks. You can optionally provided an animation
     * for collapsing, expanding, or both.
     * @param collapseIconDrawable Drawable for the icon in the collapsed state.
     * @param expandIconDrawable Drawable for the icon in the expanded state.
     * @param collapseAnim Animation for collapsing the button.
     * @param expandAnim Animation for expanding the button.
     */
    fun setIcon(
        collapseIconDrawable: Drawable,
        expandIconDrawable: Drawable? = null,
        collapseAnim: Animation? = null,
        expandAnim: Animation? = null
    ) {
        collapseAnimation = collapseAnim
        expandAnimation = expandAnim
        collapseIcon = collapseIconDrawable
        expandIcon = expandIconDrawable
        state.setIcon(button, collapseIcon, expandIcon)
    }

    /**
     * Set the scale type of the SpeedDial's icon.
     * @param type ScaleType for the button's icon.
     * @see ImageView.ScaleType
     */
    fun setScaleType(type: ImageView.ScaleType) {
        button.scaleType = type
    }

    /**
     * Change the color of the SpeedDial.
     * @param color provided color resource id.
     * @see ColorRes
     */
    fun setFabColor(@ColorRes color: Int) {
        button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, color))
    }

    /**
     * Once a SpeedDial is clicked, it covers the screen in an overlay. Use this method to change the color
     * of the provided overlay.
     * @param color provided color resource id.
     * @see ColorRes
     */
    fun setOverlayColor(@ColorRes color: Int) {
        val colors: Array<ColorDrawable> = arrayOf(
            ColorDrawable(ContextCompat.getColor(context,
                COLOR_COLLAPSED_BACKGROUND
            )),
            ColorDrawable(ContextCompat.getColor(context, color)))

        backgroundTransition = TransitionDrawable(colors).also { background = it }
    }

    @Synchronized
    private fun changeState() {
        state = state.change(
            button,
            backgroundTransition,
            ANIMATION_DURATION,
            collapseIcon,
            expandIcon,
            collapseAnimation,
            expandAnimation,
            actions
        )
    }

    private enum class State {
        COLLAPSED {
            override fun change(
                button: ImageButton,
                backgroundTransition: TransitionDrawable?,
                transitionDuration: Int,
                collapseIcon: Drawable?,
                expandIcon: Drawable?,
                collapseAnim: Animation?,
                expandAnim: Animation?,
                actions: Collection<SpeedDialAction>
            ): State {
                backgroundTransition?.startTransition(transitionDuration)
                expandAnim?.let { button.startAnimation(it) }
                setIcon(button, collapseIcon, expandIcon)
                actions.forEach { it.show() }
                return EXPANDED
            }

            override fun setIcon(button: ImageButton, collapseIcon: Drawable?, expandIcon: Drawable?) {
                button.setImageDrawable(collapseIcon)
            }
        },
        EXPANDED {
            override fun change(
                button: ImageButton,
                backgroundTransition: TransitionDrawable?,
                transitionDuration: Int,
                collapseIcon: Drawable?,
                expandIcon: Drawable?,
                collapseAnim: Animation?,
                expandAnim: Animation?,
                actions: Collection<SpeedDialAction>
            ): State {
                backgroundTransition?.reverseTransition(transitionDuration)
                collapseAnim?.let { button.startAnimation(it) }
                setIcon(button, collapseIcon, expandIcon)
                actions.forEach { it.hide() }
                return COLLAPSED
            }

            override fun setIcon(button: ImageButton, collapseIcon: Drawable?, expandIcon: Drawable?) {
                expandIcon?.let { button.setImageDrawable(it) }
            }
        };

        abstract fun change(
            button: ImageButton,
            backgroundTransition: TransitionDrawable?,
            transitionDuration: Int,
            collapseIcon: Drawable?,
            expandIcon: Drawable?,
            collapseAnim: Animation?,
            expandAnim: Animation?,
            actions: Collection<SpeedDialAction>
        ): State

        abstract fun setIcon(button: ImageButton, collapseIcon: Drawable?, expandIcon: Drawable?)
    }

    companion object {
        private val scaleTypeMap: HashMap<Int, ImageView.ScaleType> = run {
            val map: HashMap<Int, ImageView.ScaleType> = hashMapOf()
            map[0] = ImageView.ScaleType.MATRIX
            map[1] = ImageView.ScaleType.FIT_XY
            map[2] = ImageView.ScaleType.FIT_START
            map[3] = ImageView.ScaleType.FIT_CENTER
            map[4] = ImageView.ScaleType.FIT_END
            map[5] = ImageView.ScaleType.CENTER
            map[6] = ImageView.ScaleType.CENTER_CROP
            map[7] = ImageView.ScaleType.CENTER_INSIDE
            return@run map
        }

        /**
         * Safe way to get scale type from map.
         * @return Returns ScaleType associated with provided value,
         * defaults to [ImageView.ScaleType.CENTER] if an invalid value.
         */
        private fun getScaleType(value: Int): ImageView.ScaleType {
            scaleTypeMap[value]?.let {
                return it
            } ?: return DEFAULT_SCALE_TYPE
        }

        private val DEFAULT_SCALE_TYPE = ImageView.ScaleType.CENTER

        private const val DEFAULT_SCALE_TYPE_VALUE = 5

        private const val ANIMATION_DURATION = 150

        private const val FAB_HORIZONTAL_MARGIN = 16F
        private const val FAB_VERTICAL_MARGIN = 16F
        private const val FAB_DIMENSION = 56F

        private const val RESOURCE_ID_NULL: Int = 0
        private const val ATTRIBUTE_NOT_SET: Int = Int.MIN_VALUE
        private const val MARGIN_NOT_SET: Float = 0.0F

        @IdRes private val FAB_ID = R.id.speed_dial_main_button

//        @AnimatorRes private val BUTTON_ANIMATOR = R.animator.animator_button

//        @DrawableRes private val DRAWABLE_FAB_ICON = R.drawable.ic_add
        @DrawableRes private val BACKGROUND_DRAWABLE = R.drawable.background_circle_ripple

//        @AnimRes private val ANIMATION_FAB_COLLAPSE = R.anim.fab_rotate_backward
//        @AnimRes private val ANIMATION_FAB_EXPAND = R.anim.fab_rotate_forward
//        @AnimRes private val ANIMATION_FAB_SHOW = R.anim.fab_slide_up
//        @AnimRes private val ANIMATION_FAB_HIDE = R.anim.fab_slide_down

        @ColorRes private val COLOR_COLLAPSED_BACKGROUND = R.color.speed_dial_overlay_collapse_default_color
        @ColorRes private val COLOR_EXPANDED_BACKGROUND = R.color.speed_dial_overlay_expand_default_color

        @StyleableRes private val STYLEABLE_RES = R.styleable.SpeedDial
        @StyleableRes private val STYLEABLE_OVERLAY_COLOR = R.styleable.SpeedDial_overlayColor
        @StyleableRes private val STYLEABLE_FAB_COLOR = R.styleable.SpeedDial_fabColor
        @StyleableRes private val STYLEABLE_FAB_MARGIN = R.styleable.SpeedDial_fabMargin
        @StyleableRes private val STYLEABLE_FAB_MARGIN_TOP = R.styleable.SpeedDial_fabMarginTop
        @StyleableRes private val STYLEABLE_FAB_MARGIN_BOTTOM = R.styleable.SpeedDial_fabMarginBottom
        @StyleableRes private val STYLEABLE_FAB_MARGIN_START = R.styleable.SpeedDial_fabMarginStart
        @StyleableRes private val STYLEABLE_FAB_MARGIN_END = R.styleable.SpeedDial_fabMarginTop
        @StyleableRes private val STYLEABLE_FAB_COLLAPSED_ICON = R.styleable.SpeedDial_collapsedIcon
        @StyleableRes private val STYLEABLE_FAB_EXPANDED_ICON = R.styleable.SpeedDial_expandedIcon
        @StyleableRes private val STYLEABLE_FAB_COLLAPSE_ANIMATION = R.styleable.SpeedDial_collapseAnim
        @StyleableRes private val STYLEABLE_FAB_EXPAND_ANIMATION = R.styleable.SpeedDial_expandAnim
        @StyleableRes private val STYLEABLE_FAB_SHOW_ANIMATION = R.styleable.SpeedDial_showAnim
        @StyleableRes private val STYLEABLE_FAB_HIDE_ANIMATION = R.styleable.SpeedDial_hideAnim
        @StyleableRes private val STYLEABLE_SCALE_TYPE = R.styleable.SpeedDial_scaleType
    }
}