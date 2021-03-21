package com.softsquared.template.kotlin.src.main.schedulefind.adapter

interface IScheduleCategoryRecyclerView {

    //카테고리별일정으로 이동
    fun onItemMoveBtnClicked(position: Int)

    //칼라값 가져오기
    fun onColor() : String
}