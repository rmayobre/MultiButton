package com.multibutton.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.multibutton.library.HideOnScrollBehavior
import com.multibutton.library.SpeedDial
import com.multibutton.library.SpeedDialAction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var button: SpeedDial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MAIN_ACTIVITY)

        button = findViewById(R.id.speedDialButton)
        with(button) {
            addAction(SpeedDialAction.Builder(this@MainActivity)
//                .setId(FAB_1)
                .setImage(FAB_1_DRAWABLE)
                .setLabel("test 1")
                .setOnClickListener(this@MainActivity)
                .setShowAnimation(R.anim.fade_in)
                .setHideAnimation(R.anim.fade_out)
                .build())
            addAction(SpeedDialAction.Builder(this@MainActivity)
//                .setId(FAB_2)
                .setImage(FAB_1_DRAWABLE)
                .setLabel("test 2")
                .setOnClickListener(this@MainActivity)
                .setShowAnimation(R.anim.fade_in)
                .setHideAnimation(R.anim.fade_out)
                .build())
            addAction(SpeedDialAction.Builder(this@MainActivity)
//                .setId(FAB_3)
                .setImage(FAB_1_DRAWABLE)
                .setLabel("test 3")
                .setOnClickListener(this@MainActivity)
                .setShowAnimation(R.anim.fade_in)
                .setHideAnimation(R.anim.fade_out)
                .build())
            addAction(SpeedDialAction.Builder(this@MainActivity)
//                .setId(FAB_4)
                .setImage(FAB_1_DRAWABLE)
                .setLabel("test 4")
                .setOnClickListener(this@MainActivity)
                .setShowAnimation(R.anim.fade_in)
                .setHideAnimation(R.anim.fade_out)
                .build())
            addAction(SpeedDialAction.Builder(this@MainActivity)
//                .setId(FAB_5)
                .setImage(FAB_1_DRAWABLE)
                .setLabel("test 5")
                .setOnClickListener(this@MainActivity)
                .setShowAnimation(R.anim.fade_in)
                .setHideAnimation(R.anim.fade_out)
                .build())
        }

        val texts: MutableList<String> = mutableListOf()
        texts.add("Text 1")
        texts.add("Text 2")
        texts.add("Text 3")
        texts.add("Text 4")
        texts.add("Text 5")
        texts.add("Text 6")
        texts.add("Text 7")
        texts.add("Text 1")
        texts.add("Text 2")
        texts.add("Text 3")
        texts.add("Text 4")
        texts.add("Text 5")
        texts.add("Text 6")
        texts.add("Text 7")
        texts.add("Text 1")
        texts.add("Text 2")
        texts.add("Text 3")
        texts.add("Text 4")
        texts.add("Text 5")
        texts.add("Text 6")
        texts.add("Text 7")

        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = TextAdapter(texts)
        }

        HideOnScrollBehavior.from(speedDialButton).hideAtEnd(false)
    }

//    override fun on

    override fun onStart() {
        super.onStart()
//        speedDialButton.hide()
    }

    override fun onClick(v: View) {
        Toast.makeText(this, "Button ID clicked -> ${v.id}", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private var TAG = MainActivity::class.java.simpleName
        @LayoutRes private const val MAIN_ACTIVITY = R.layout.activity_main
        @IdRes private var FAB_1 = R.id.button1
        @IdRes private var FAB_2 = R.id.button2
        @IdRes private var FAB_3 = R.id.button3
        @IdRes private var FAB_4 = R.id.button4
        @IdRes private var FAB_5 = R.id.button5
        @DrawableRes private var FAB_1_DRAWABLE = R.drawable.ic_android_white
    }
}

