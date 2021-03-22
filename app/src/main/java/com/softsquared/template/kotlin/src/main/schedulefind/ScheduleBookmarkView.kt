package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.mypage.models.RestScheduleCountResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleCountResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryResponse
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse

interface ScheduleBookmarkView {

    //일정찾기 - 전체일정조회
    fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse)
    fun onGetScheduleBookmarkFail(message: String)


}