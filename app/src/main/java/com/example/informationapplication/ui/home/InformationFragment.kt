package com.example.informationapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.informationapplication.databinding.FragmentLeftBinding
import com.example.informationapplication.ui.home.adapter.ViewPagerAdapter
import com.example.informationapplication.ui.home.view.SubInformationFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class InformationFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private val fragmentList = ArrayList<Fragment>()
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val titleList: List<String> = listOf("国家政策","院校政策","考研动态","复试指导","专硕巡展","推免面试")

    private var _binding: FragmentLeftBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLeftBinding.inflate(inflater, container, false)
        val root: View = binding.root
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        for (item in titleList) {
            fragmentList.add(SubInformationFragment.newInstance(item))
        }
        viewPagerAdapter = ViewPagerAdapter(this, fragmentList, titleList)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(
            tabLayout,
            viewPager
        ) { tab, position ->
            run {
                tab.text = titleList[position]
            }
        }.attach()


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}