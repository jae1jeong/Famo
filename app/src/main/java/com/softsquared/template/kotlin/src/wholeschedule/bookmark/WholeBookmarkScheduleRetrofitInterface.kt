package com.softsquared.template.kotlin.src.wholeschedule.bookmark

import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkResponse
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WholeBookmarkScheduleRetrofitInterface {

    //전체 즐겨찾기일정 조회
    @GET("schedules/picks")
    fun getScheduleBookmark(@Query("offset") offset : Int,
                            @Query("limit") limit : Int ) :
            Call<ScheduleBookmarkResponse>
}