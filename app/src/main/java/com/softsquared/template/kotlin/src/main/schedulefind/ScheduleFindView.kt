package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.mypage.models.RestScheduleCountResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleCountResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryResponse

interface ScheduleFindView {

    //일정찾기 - 전체일정조회
    fun onGetWholeScheduleInquirySuccess(response: WholeScheduleInquiryResponse)
    fun onGetWholeScheduleInquiryFail(message: String)

    //일정찾기 - 즐겨찾기
    fun onPostBookmarkSuccess(response: BaseResponse)
    fun onPostBookmarkFail(message: String)

    // 전체 일정수 조회
    fun onGetWholeScheduleCountSuccess(response: WholeScheduleCountResponse)
    fun onGetWholeScheduleCountFailure(message:String)

}