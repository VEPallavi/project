package com.utalli.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.utalli.fragment.RecentToursFragment
import com.utalli.fragment.UpcomingToursFragment


/*
class MyToursViewPager : FragmentStatePagerAdapter() {
}*/


class MyToursViewPager(fm: FragmentManager, internal var tabCount: Int) : FragmentStatePagerAdapter(fm) {

    //Overriding method getItem
    override fun getItem(position: Int): Fragment {
        //Returning the current tabs
        when (position) {
            0 -> {
                return UpcomingToursFragment()
            }
            1 -> {
                return RecentToursFragment()
            }
            else -> return null!!
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}