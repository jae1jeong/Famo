package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleSearchResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.TodayRestScheduleResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleCountResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryResponse
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse

interface BookmarkView {

    //일정찾기 - 즐겨찾기
    fun onPostBookmarkSuccess(response: BaseResponse)
    fun onPostBookmarkFail(message: String)


}