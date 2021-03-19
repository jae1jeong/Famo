package com.softsquared.template.kotlin.src.wholeschedule

import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryResponse
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WholeScheduleRetrofitInterface {

    //최근 일정 조회
    @GET("schedules/recents")
    fun getLatelyWholeScheduleInquiry(@Query("offset") offset : Int,
                                @Query("limit") limit : Int ) :
            Call<LatelyScheduleInquiryResponse>
}