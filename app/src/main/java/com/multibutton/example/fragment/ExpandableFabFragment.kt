package com.multibutton.example.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.multibutton.example.R
import com.multibutton.example.TextAdapter
import com.multibutton.library.expanding.ExpandableFAB

class ExpandableFabFragment : Fragment(), View.OnClickListener {

    private lateinit var recyclerView: RecyclerView

    private lateinit var button: ExpandableFAB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(FRAGMENT_LAYOUT, container, false).apply {

        /* RecyclerView setup */
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = TextAdapter(TEXT_LIST)
            setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY < oldScrollY && button.state == ExpandableFAB.State.COLLAPSED) {
                    button.changeState()
                } else if (scrollY > oldScrollY && button.state == ExpandableFAB.State.EXPANDED) {
                    button.changeState()
                }
            }
        }

        /* ExpandableFAB setup */
        button = findViewById<ExpandableFAB>(R.id.expandable_fab).apply {
            setOnClickListener(this@ExpandableFabFragment)
        }
    }

    override fun onClick(v: View) {
        with(context) {
            Toast.makeText(this, "Email button was clicked.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @LayoutRes private const val FRAGMENT_LAYOUT = R.layout.fragment_expandable_fab

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