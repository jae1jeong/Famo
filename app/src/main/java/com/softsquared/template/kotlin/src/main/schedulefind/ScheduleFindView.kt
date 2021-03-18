package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryResponse

interface ScheduleFindView {

    //일정찾기 - 전체일정조회
    fun onGetWholeScheduleInquirySuccess(response: WholeScheduleInquiryResponse)
    fun onGetWholeScheduleInquiryFail(message: String)

}