package com.softsquared.template.kotlin.src.main.mypage

import com.softsquared.template.kotlin.src.main.mypage.models.DoneScheduleCountResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.main.mypage.models.RestScheduleCountResponse
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

    // 남은 일정수 조회
    @GET("schedules/left-over")
    fun getRestScheduleCount(@Query("filter") date:String):Call<RestScheduleCountResponse>

    // 총 해낸 일정 조회
    @GET("schedules/counts")
    fun getDoneScheduleCount():Call<DoneScheduleCountResponse>
}