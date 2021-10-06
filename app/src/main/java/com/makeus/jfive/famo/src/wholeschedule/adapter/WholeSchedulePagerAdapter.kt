package com.makeus.jfive.famo.src.wholeschedule.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class WholeSchedulePagerAdapter(fm:FragmentManager):FragmentPagerAdapter(fm) {
    val fragmentList:MutableList<Fragment> = arrayListOf()
    val titleList:MutableList<String> = arrayListOf()

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment:Fragment,title:String){
        fragmentList.add(fragment)
        titleList.add(title)
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

}