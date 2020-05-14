package com.multibutton.library

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HideOnScrollBehavior: CoordinatorLayout.Behavior<SpeedDial> {
    private var listener: ScrollListener = ScrollListener()

    constructor() : super()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: SpeedDial, dependency: View): Boolean {
        return when (dependency) {
            is RecyclerView -> dependency.isScrollable { register(child, dependency) }
            is ViewGroup -> findRecyclerView(dependency, child)
            else -> false
        }
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: SpeedDial, dependency: View) {
        (dependency as RecyclerView).removeOnScrollListener(listener)
    }

    /**
     * Choose to hide speedDial at the end of RecyclerView.
     * @param hide True to hide at the end of a RecyclerView, False to keep button present at the end of the RecyclerView.
     */
    fun hideAtEnd(hide: Boolean) {
        listener.hideAtEnd = hide
    }

    /**
     * Register this behavior to a speedDial and a RecyclerView.
     * @param speedDial the view to be register to behavior.
     * @param recyclerView the RecyclerView the behavior will listener to for state changes.
     */
    fun register(speedDial: SpeedDial, recyclerView: RecyclerView) {
        listener.speedDial = speedDial
        recyclerView.addOnScrollListener(listener)
    }

    private fun findRecyclerView(viewGroup: ViewGroup, speedDial: SpeedDial): Boolean {
        for (i in 0..viewGroup.childCount) {
            when(val view = viewGroup.getChildAt(i)) {
                is RecyclerView -> return view.isScrollable { register(speedDial, view) }
                is ViewGroup    -> if (findRecyclerView(view, speedDial)) return true
            }
        }
        return false
    }

    private inline fun RecyclerView.isScrollable(block: (RecyclerView) -> Unit): Boolean {
        return (this.layoutManager as LinearLayoutManager?)?.let { layoutManager ->
            this.adapter?.let { adapter ->
                return if (layoutManager.findLastCompletelyVisibleItemPosition() < adapter.itemCount - 1) {
                    block(this)
                    true
                } else {
                    false
                }
            } ?: false
        } ?: false
    }

    inner class ScrollListener: RecyclerView.OnScrollListener() {
        var speedDial: SpeedDial? = null
        var hideAtEnd: Boolean = true

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (hideAtEnd) {
                    if (recyclerView.canScrollVertically(1)) {
                        speedDial?.show()
                    }
                } else {
                    speedDial?.show()
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0 || dy < 0) {
                speedDial?.hide()
            }
        }
    }

    companion object {
        fun from(view: View): HideOnScrollBehavior {
            val params = view.layoutParams
            if (params !is CoordinatorLayout.LayoutParams) {
                throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
            } else {
                val behavior = params.behavior
                return if (behavior !is HideOnScrollBehavior) {
                    throw IllegalArgumentException("The view is not associated with RecyclerViewBehavior")
                } else {
                    behavior
                }
            }
        }
    }
}