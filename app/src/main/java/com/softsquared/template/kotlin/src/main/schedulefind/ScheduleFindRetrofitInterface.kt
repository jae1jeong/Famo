package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleFindRetrofitInterface {

    //일정찾기 - 전체일정조회
    @GET("schedules")
    fun getWholeScheduleInquiry() : Call<WholeScheduleInquiryResponse>

}