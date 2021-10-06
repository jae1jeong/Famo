package com.makeus.jfive.famo.src.main.monthly.calendar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.BaseFragment
import com.makeus.jfive.famo.databinding.FragmentCalendarBinding
import com.makeus.jfive.famo.src.presentation.main.MainActivity

class CalendarFragment:BaseFragment<FragmentCalendarBinding>(FragmentCalendarBinding::bind, R.layout.fragment_calendar) {
    private lateinit var mContext:Context
    var pageIndex = 0
    var TAG = javaClass.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainActivity){
            mContext = context
        }
    }

    private fun initView(view:View){
        pageIndex -= (Int.MAX_VALUE / 2)
        Log.d(TAG, "initView: $pageIndex")

    }

}