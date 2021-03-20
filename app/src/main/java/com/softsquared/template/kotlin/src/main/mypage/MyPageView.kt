package com.softsquared.template.kotlin.src.main.mypage

import com.softsquared.template.kotlin.src.main.mypage.models.DoneScheduleCountResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.main.mypage.models.RestScheduleCountResponse

interface MyPageView {

    //상단멘트
//    fun onGetMyPageCommentsSuccess(response: MyPageCommentsResponse)
//    fun onGetMyPageCommentsFail(message: String)

    //마이페이지 조회
    fun onGetMyPageSuccess(response: MyPageResponse)
    fun onGetMyPageFail(message: String)

    // 남은 일정수 조회
    fun onGetRestScheduleCountSuccess(response:RestScheduleCountResponse)
    fun onGetRestScheduleCountFailure(message:String)

    // 해낸 일정수 조회
    fun onGetDoneScheduleCountSuccess(response:DoneScheduleCountResponse)
    fun onGetDoneScheduleCountFailure(message: String)
}