package com.softsquared.template.kotlin.src.main.today

import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse
import retrofit2.Call
import retrofit2.http.GET

interface TodayRetrofitInterface {
    @GET("schedules")
    fun getTodayMemos(): Call<ScheduleItemsResponse>
}