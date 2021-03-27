package com.softsquared.template.kotlin.src.main.today

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.models.DetailMemoResponse
import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse
import com.softsquared.template.kotlin.src.main.today.models.TopCommentResponse

interface TodayView {
    fun onGetScheduleItemsSuccess(response:ScheduleItemsResponse)
    fun onGetScheduleItemsFailure(message:String)
    fun onDeleteMemoSuccess(response: BaseResponse,scheduleID:Int)
    fun onDeleteMemoFailure(message: String)
    fun onPostItemCheckSuccess(response: BaseResponse)
    fun onPostItemCheckFailure(message: String)
    fun onGetUserTopCommentSuccess(response:TopCommentResponse)
    fun onGetUserTopCommentFailure(message: String)
    fun onPostSchedulePositionSuccess(response: BaseResponse)
    fun onPostSchedulePositionFailure(message: String)
    fun onGetDetailMemoSuccess(response: DetailMemoResponse)
    fun onGetDetailMemoFailure(message: String)
}