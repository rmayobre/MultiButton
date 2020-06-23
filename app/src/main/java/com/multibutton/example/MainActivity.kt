package com.multibutton.example

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    private lateinit var viewPager: ViewPager

    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sectionsPagerAdapter = SectionsPagerAdapter(
            this,
            supportFragmentManager
        )

        viewPager = findViewById<ViewPager>(R.id.view_pager).apply {
            adapter = sectionsPagerAdapter
        }

        tabLayout = findViewById<TabLayout>(R.id.tabs).apply {
            setupWithViewPager(viewPager)
        }
    }
}