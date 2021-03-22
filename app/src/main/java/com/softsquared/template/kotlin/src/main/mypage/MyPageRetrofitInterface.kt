package com.softsquared.template.kotlin.src.main.mypage

import com.softsquared.template.kotlin.src.main.mypage.models.DoneScheduleCountResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.main.mypage.models.RestScheduleCountResponse
import com.softsquared.template.kotlin.src.main.mypage.models.TotalScheduleCountResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MyPageRetrofitInterface {

    //상단멘트
//    @GET("profiles/comments")
//    fun getMyPageComments() : Call<MyPageCommentsResponse>

    //프로필 조회
    @GET("profiles")
    fun getMyPage() : Call<MyPageResponse>

    // 남은 일정수 조회(전채/오늘)
    @GET("schedules/left-over")
    fun getRestScheduleCount(@Query("filter") date:String):Call<RestScheduleCountResponse>

    // 전체 일정/해낸일정 조회
    @GET("schedules/counts")
    fun getTotalScheduleCount():Call<TotalScheduleCountResponse>
}