package com.softsquared.template.kotlin.src.main.today

import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse

interface TodayView {
    fun onGetScheduleItemsSuccess(response:ScheduleItemsResponse)
    fun onGetScheduleItemsFailure(message:String)
}