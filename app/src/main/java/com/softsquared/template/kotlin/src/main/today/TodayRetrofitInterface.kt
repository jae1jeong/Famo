package com.softsquared.template.kotlin.src.main.today

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse
import retrofit2.Call
import retrofit2.http.*

interface TodayRetrofitInterface {
    @GET("schedules/today")
    fun getTodayMemos(): Call<ScheduleItemsResponse>

    @PUT("schedules/{scheduleID}")
    fun deleteMemo(@Path("scheduleID") scheduleID:Int):Call<BaseResponse>

    @POST("schedules/achievements/today")
    fun postItemCheck(@Body scheduleID: Int):Call<BaseResponse>

}