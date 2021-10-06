package com.makeus.jfive.famo.src.main.schedulefind

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.ScheduleSearchResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.TodayRestScheduleResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.WholeScheduleCountResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.WholeScheduleInquiryResponse
import com.makeus.jfive.famo.src.wholeschedule.models.LatelyScheduleInquiryResponse

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

    //최근
    fun onGetLatelyScheduleFindInquirySuccess(response: LatelyScheduleInquiryResponse)
    fun onGetLatelySchedulefindInquiryFail(message: String)

    //최근
    fun onGetTodayRestScheduleSuccess(response: TodayRestScheduleResponse)
    fun onGetTodayRestScheduleFail(message: String)

    //일정검색
    fun onGetScheduleSearchSuccess(response: ScheduleSearchResponse)
    fun onGetScheduleSearchFail(message: String)

}