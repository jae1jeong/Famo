package com.makeus.jfive.famo.src.main.schedulefind

import com.makeus.jfive.famo.src.main.schedulefind.models.ScheduleSearchRequest
import com.makeus.jfive.famo.src.main.schedulefind.models.ScheduleSearchResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface ScheduleFindCategoryRetrofitInterface {

    // 일정검색
    @GET("schedules/search")
    fun getScheduleSearch(@Body scheduleSearchRequest : ScheduleSearchRequest): Call<ScheduleSearchResponse>
}