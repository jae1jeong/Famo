package com.makeus.jfive.famo.src.main.schedulefind

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.*
import com.makeus.jfive.famo.src.wholeschedule.models.LatelyScheduleInquiryResponse
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

    //일정찾기 - 카테고리 즐겨찾기on/off
    @POST("schedules/picks")
    fun postBookmark(@Body bookmarkRequest: BookmarkRequest) : Call<BaseResponse>

    // 전체일정조회수
    @GET("schedules/counts")
    fun getWholeScheduleCount():Call<WholeScheduleCountResponse>

    //최근 일정 조회
    @GET("schedules/recents")
    fun getLatelyScheduleInquiry(@Query("offset") offset : Int,
                                      @Query("limit") limit : Int ) :
            Call<LatelyScheduleInquiryResponse>

    // 남은 일정수 조회(전채/오늘)
    @GET("schedules/left-over")
    fun getRestScheduleCount(@Query("filter") date:String):Call<TodayRestScheduleResponse>

    // 일정검색
    @GET("schedules/search")
    fun getScheduleSearch(@Query("searchWord") searchWord : String):Call<ScheduleSearchResponse>


}