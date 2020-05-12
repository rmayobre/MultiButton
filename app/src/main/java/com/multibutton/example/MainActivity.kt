package com.multibutton.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.multibutton.library.HideOnScrollBehavior
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MAIN_ACTIVITY)

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

//        with(speedDialButton) {
//            addAction(
//                SpeedDialAction.Attributes(FAB_1, FAB_1_DRAWABLE)
//                    .text("test 1"))
//
//            addAction(
//                SpeedDialAction.Attributes(FAB_2, FAB_1_DRAWABLE)
//                    .text("test 2"))
//
//            addAction(
//                SpeedDialAction.Attributes(FAB_3, FAB_1_DRAWABLE)
//                    .text("test 3"))
//
//            addAction(
//                SpeedDialAction.Attributes(FAB_4, FAB_1_DRAWABLE)
//                    .text("test 4"))
//
//            addAction(
//                SpeedDialAction.Attributes(FAB_5, FAB_1_DRAWABLE)
//                    .text("test 5"))
//            addListener(View.OnClickListener {
//                Log.d(TAG, "Action Clicked")
//            })
//        }

        HideOnScrollBehavior.from(speedDialButton).hideAtEnd(false)
    }

    override fun onStart() {
        super.onStart()
        speedDialButton.hide()
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

