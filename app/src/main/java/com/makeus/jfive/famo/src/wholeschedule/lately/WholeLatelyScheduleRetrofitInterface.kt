package com.makeus.jfive.famo.src.wholeschedule.lately

import com.makeus.jfive.famo.src.wholeschedule.models.LatelyScheduleInquiryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WholeLatelyScheduleRetrofitInterface {

    //최근 일정 조회
    @GET("schedules/recents")
    fun getLatelyWholeScheduleInquiry(@Query("offset") offset : Int,
                                      @Query("limit") limit : Int ) :
            Call<LatelyScheduleInquiryResponse>
}