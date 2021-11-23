package com.example.taskreminder.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.taskreminder.fragments.CompletedFragment
import com.example.taskreminder.fragments.PendingFragment
import com.example.taskreminder.fragments.TodayFragment

class PageAdapter(fm: FragmentManager, private val totalTabs: Int) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return TodayFragment()
        } else if (position == 1) return PendingFragment()
        return CompletedFragment()
    }

    override fun getCount(): Int {
        return totalTabs
    }
}
