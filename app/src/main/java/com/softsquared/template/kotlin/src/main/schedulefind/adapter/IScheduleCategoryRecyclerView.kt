package com.softsquared.template.kotlin.src.main.schedulefind.adapter

interface IScheduleCategoryRecyclerView {

    //카테고리별일정으로 이동
    fun onItemMoveBtnClicked(scheduleCategoryID : Int)

    //칼라값 가져오기
    fun onColor() : ArrayList<String>

    // 카테고리가 두 번 선택 됬을떄 프래그먼트 전환
    fun onClickedTwice()
}