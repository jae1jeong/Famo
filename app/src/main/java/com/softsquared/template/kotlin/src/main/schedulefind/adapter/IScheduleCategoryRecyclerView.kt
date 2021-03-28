package com.softsquared.template.kotlin.src.main.schedulefind.adapter

import com.softsquared.template.kotlin.src.main.schedulefind.SchedulefindFilterBottomDialogFragment

interface IScheduleCategoryRecyclerView {

    //카테고리별일정으로 이동
    fun onItemMoveBtnClicked(scheduleCategoryID : Int)

    //칼라값 가져오기
    fun onColor() : ArrayList<String>

    fun onMoveFilterFragment(scheduleCategoryID : Int)

    fun onScheduleDetail()
}