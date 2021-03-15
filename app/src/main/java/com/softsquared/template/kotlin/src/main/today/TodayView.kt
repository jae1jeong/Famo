package com.softsquared.template.kotlin.src.main.today

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse

interface TodayView {
    fun onGetScheduleItemsSuccess(response:ScheduleItemsResponse)
    fun onGetScheduleItemsFailure(message:String)
    fun onDeleteMemoSuccess(response: BaseResponse,scheduleID:Int)
    fun onDeleteMemoFailure(message: String)
    fun onPostItemCheckSuccess(response: BaseResponse)
    fun onPostItemCheckFailure(message: String)
}