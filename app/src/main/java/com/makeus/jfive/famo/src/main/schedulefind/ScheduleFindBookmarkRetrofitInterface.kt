package com.makeus.jfive.famo.src.main.schedulefind

import com.makeus.jfive.famo.src.main.schedulefind.models.ScheduleBookmarkResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleFindBookmarkRetrofitInterface {

    //일정찾기 - 즐겨찾기
    @GET("schedules/picks")
    fun getScheduleBookmark(@Query("offset") offset : Int,
                                @Query("limit") limit : Int ) :
            Call<ScheduleBookmarkResponse>
}