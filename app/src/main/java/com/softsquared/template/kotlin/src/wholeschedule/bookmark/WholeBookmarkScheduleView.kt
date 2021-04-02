package com.softsquared.template.kotlin.src.wholeschedule.bookmark

import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkResponse

interface WholeBookmarkScheduleView {

    //전체 즐겨찾기일정조회
    fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse)
    fun onGetScheduleBookmarkFail(message: String)
}