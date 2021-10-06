package com.makeus.jfive.famo.src.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makeus.jfive.famo.src.main.monthly.MonthlyFragment
import com.makeus.jfive.famo.src.main.schedulefind.ScheduleFindFragment
import com.makeus.jfive.famo.src.presentation.today.TodayFragment

class MainPagerAdapter(fa:FragmentActivity):FragmentStateAdapter(fa) {
    val fragmentList:MutableList<Fragment> = arrayListOf()

    init {
        fragmentList.add(MonthlyFragment())
        fragmentList.add(TodayFragment())
        fragmentList.add(ScheduleFindFragment())
    }


    override fun getItemCount(): Int  = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MonthlyFragment()
            1 -> TodayFragment()
            2 -> ScheduleFindFragment()
            else -> throw IllegalStateException()
        }
    }

}