package com.multibutton.library

import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.view.animation.Animation
import android.widget.ImageButton

enum class State {
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
            collapseAnim?.let { button.startAnimation(it) }
            setIcon(button, collapseIcon, expandIcon)
            actions.forEach { TODO("Fade action buttons.") }
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
            expandAnim?.let { button.startAnimation(it) }
            setIcon(button, collapseIcon, expandIcon)
            actions.forEach { TODO("Fade action buttons.") }
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