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
     * Choose to hide SpeedDial at the end of RecyclerView.
     * @param hide True to hide at the end of a RecyclerView, False to keep button present at the end of the RecyclerView.
     */
    fun hideAtEnd(hide: Boolean) {
        listener.hideAtEnd = hide
    }

    /**
     * Register this behavior to a SpeedDial and a RecyclerView.
     * @param SpeedDial the button to be register to behavior.
     * @param recyclerView the RecyclerView the behavior will listener to for state changes.
     */
    fun register(SpeedDial: SpeedDial, recyclerView: RecyclerView) {
        listener.SpeedDial = SpeedDial
        recyclerView.addOnScrollListener(listener)
    }

    private fun findRecyclerView(viewGroup: ViewGroup, SpeedDial: SpeedDial): Boolean {
        for (i in 0..viewGroup.childCount) {
            when(val view = viewGroup.getChildAt(i)) {
                is RecyclerView -> return view.isScrollable { register(SpeedDial, view) }
                is ViewGroup    -> if (findRecyclerView(view, SpeedDial)) return true
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
        var SpeedDial: SpeedDial? = null
        var hideAtEnd: Boolean = true

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (hideAtEnd) {
                    if (recyclerView.canScrollVertically(1)) {
                        SpeedDial?.show()
                    }
                } else {
                    SpeedDial?.show()
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0 || dy < 0) {
                SpeedDial?.hide()
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