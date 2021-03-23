package com.softsquared.template.kotlin.src.mypage

import com.softsquared.template.kotlin.src.mypage.models.MonthsAchievementsResponse
import com.softsquared.template.kotlin.src.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.mypage.models.RestScheduleCountResponse
import com.softsquared.template.kotlin.src.mypage.models.TotalScheduleCountResponse

interface MyPageView {

    //상단멘트
//    fun onGetMyPageCommentsSuccess(response: MyPageCommentsResponse)
//    fun onGetMyPageCommentsFail(message: String)

    //마이페이지 조회
    fun onGetMyPageSuccess(response: MyPageResponse)
    fun onGetMyPageFail(message: String)

    // 남은 일정수 조회
    fun onGetRestScheduleCountSuccess(response: RestScheduleCountResponse)
    fun onGetRestScheduleCountFailure(message:String)

    // 전체 일정/해낸 일정수 조회
    fun onGetTotalScheduleCountSuccess(response: TotalScheduleCountResponse)
    fun onGetTotalScheduleCountFailure(message: String)

    // 월별달성률
    fun onGetMonthsAchievmentsSuccess(response: MonthsAchievementsResponse)
    fun onGetMonthsAchievmentsFailure(message: String)
}