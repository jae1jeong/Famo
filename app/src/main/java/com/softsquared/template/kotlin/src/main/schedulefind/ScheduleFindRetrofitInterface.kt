package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ScheduleFindRetrofitInterface {

    //일정찾기 - 전체일정조회
    @GET("schedules")
    fun getWholeScheduleInquiry(@Query("offset") offset : Int,
                                @Query("limit") limit : Int ) :
            Call<WholeScheduleInquiryResponse>

    //일정찾기 - 카테고리 즐겨찾기
    @POST("schedules/picks")
    fun postBoorkmark(@Body scheduleID : Int) : Call<BaseResponse>

}