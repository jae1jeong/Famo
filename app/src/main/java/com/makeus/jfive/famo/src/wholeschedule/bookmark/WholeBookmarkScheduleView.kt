package com.makeus.jfive.famo.src.wholeschedule.bookmark

import com.makeus.jfive.famo.src.main.schedulefind.models.ScheduleBookmarkResponse

interface WholeBookmarkScheduleView {

    //전체 즐겨찾기일정조회
    fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse)
    fun onGetScheduleBookmarkFail(message: String)
}