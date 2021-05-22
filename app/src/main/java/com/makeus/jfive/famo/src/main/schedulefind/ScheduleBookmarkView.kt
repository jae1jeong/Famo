package com.makeus.jfive.famo.src.main.schedulefind

import com.makeus.jfive.famo.src.main.schedulefind.models.ScheduleBookmarkResponse

interface ScheduleBookmarkView {

    //일정찾기 - 전체일정조회
    fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse)
    fun onGetScheduleBookmarkFail(message: String)


}