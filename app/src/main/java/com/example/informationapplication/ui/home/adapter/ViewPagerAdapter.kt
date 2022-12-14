package com.example.informationapplication.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    parent: Fragment,
    private val fragments: List<Fragment>,
    private val tabs: List<String>
) : FragmentStateAdapter(parent) {
    override fun getItemCount() = tabs.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}