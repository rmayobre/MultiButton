package com.multibutton.example

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.multibutton.example.fragment.ExpandableFabFragment
import com.multibutton.example.fragment.FabFragment
import com.multibutton.example.fragment.SpeedDialFragment

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = FRAGMENTS[position]

    override fun getPageTitle(position: Int): CharSequence? =
        context.resources.getString(TAB_TITLES[position])

    override fun getCount(): Int = 3

    companion object {
        private val TAB_TITLES = arrayOf(
            R.string.fab_tab_text,
            R.string.expandable_fab_text,
            R.string.speed_dial_text
        )

        private val FRAGMENTS = listOf<Fragment>(
            FabFragment(),
            ExpandableFabFragment(),
            SpeedDialFragment()
        )
    }
}