package com.multibutton.example.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.multibutton.example.R
import com.multibutton.example.TextAdapter
import com.multibutton.library.HideOnScrollBehavior
import com.multibutton.library.speeddial.SpeedDial
import com.multibutton.library.speeddial.SpeedDialAction

class SpeedDialFragment : Fragment(), View.OnClickListener {

    private lateinit var recyclerView: RecyclerView
    
    private lateinit var button: SpeedDial

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(FRAGMENT_LAYOUT, container, false).apply {

        /* RecyclerView setup */
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = TextAdapter(TEXT_LIST)
        }

        /* SpeedDial setup */
        button = findViewById<SpeedDial>(R.id.speedDialButton).apply {
            addAction(
                SpeedDialAction.Builder(context)
                    .setId(FAB_1)
                    .setImage(ACTION_DRAWABLE)
                    .setLabel("test 1")
                    .setOnClickListener(this@SpeedDialFragment)
                    .setShowAnimation(R.anim.fab_fade_in)
                    .setHideAnimation(R.anim.fab_fade_out)
                    .build())
            addAction(
                SpeedDialAction.Builder(context)
                    .setId(FAB_2)
                    .setImage(ACTION_DRAWABLE)
                    .setLabel("test 2")
                    .setOnClickListener(this@SpeedDialFragment)
                    .setShowAnimation(R.anim.fab_fade_in)
                    .setHideAnimation(R.anim.fab_fade_out)
                    .build())
            addAction(
                SpeedDialAction.Builder(context)
                    .setId(FAB_3)
                    .setImage(ACTION_DRAWABLE)
                    .setLabel("test 3")
                    .setOnClickListener(this@SpeedDialFragment)
                    .setShowAnimation(R.anim.fab_fade_in)
                    .setHideAnimation(R.anim.fab_fade_out)
                    .build())
            addAction(
                SpeedDialAction.Builder(context)
                    .setId(FAB_4)
                    .setImage(ACTION_DRAWABLE)
                    .setLabel("test 4")
                    .setOnClickListener(this@SpeedDialFragment)
                    .setShowAnimation(R.anim.fab_fade_in)
                    .setHideAnimation(R.anim.fab_fade_out)
                    .build())
            addAction(
                SpeedDialAction.Builder(context)
                    .setId(FAB_5)
                    .setImage(ACTION_DRAWABLE)
                    .setLabel("test 5")
                    .setOnClickListener(this@SpeedDialFragment)
                    .setShowAnimation(R.anim.fab_fade_in)
                    .setHideAnimation(R.anim.fab_fade_out)
                    .build())
        }

        /*
         * Setup HideOnScrollBehavior to SpeedDial.
         * Check FRAGMENT_LAYOUT XML layout for behavior declaration.
         */
        HideOnScrollBehavior.from(button).hideAtEnd(false)
    }

    override fun onClick(v: View) {
        val buttonNum = when (v.id) {
            R.id.button1 -> 1
            R.id.button2 -> 2
            R.id.button3 -> 3
            R.id.button4 -> 4
            R.id.button5 -> 5
            else -> -1
        }
        with(context) {
            if (buttonNum != -1) {
                Toast.makeText(this, "Button ID clicked -> $buttonNum", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Button could not be identified", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /* SpeedDialAction id resources */
        @IdRes private var FAB_1 = R.id.button1
        @IdRes private var FAB_2 = R.id.button2
        @IdRes private var FAB_3 = R.id.button3
        @IdRes private var FAB_4 = R.id.button4
        @IdRes private var FAB_5 = R.id.button5

        @LayoutRes private const val FRAGMENT_LAYOUT = R.layout.fragment_speed_dial

        @DrawableRes private var ACTION_DRAWABLE = R.drawable.ic_android_white
        
        private val TEXT_LIST: MutableList<String> = mutableListOf<String>().apply {
            add("Text 1")
            add("Text 2")
            add("Text 3")
            add("Text 4")
            add("Text 5")
            add("Text 6")
            add("Text 7")
            add("Text 8")
            add("Text 9")
            add("Text 10")
            add("Text 11")
            add("Text 12")
            add("Text 13")
            add("Text 14")
            add("Text 15")
            add("Text 16")
            add("Text 17")
            add("Text 18")
            add("Text 19")
            add("Text 20")
            add("Text 21")
        }
    }
}