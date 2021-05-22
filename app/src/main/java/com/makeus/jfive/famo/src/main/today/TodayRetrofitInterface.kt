package com.makeus.jfive.famo.src.main.today

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse
import com.makeus.jfive.famo.src.main.today.models.ChangePositionItemRequest
import com.makeus.jfive.famo.src.main.today.models.CheckItemRequest
import com.makeus.jfive.famo.src.main.today.models.ScheduleItemsResponse
import com.makeus.jfive.famo.src.main.today.models.TopCommentResponse
import retrofit2.Call
import retrofit2.http.*

interface TodayRetrofitInterface {
    @GET("schedules/today")
    fun getTodayMemos(): Call<ScheduleItemsResponse>

    @PUT("schedules/{scheduleID}")
    fun deleteMemo(@Path("scheduleID") scheduleID:Int):Call<BaseResponse>

    @POST("schedules/achievements/today")
    fun postItemCheck(@Body checkItemRequest: CheckItemRequest):Call<BaseResponse>

    @GET("profiles/comments")
    fun getTopComment():Call<TopCommentResponse>

    @POST("schedules/orderchanges")
    fun postChangeItemPosition(@Body changePositionItemRequest: ChangePositionItemRequest):Call<BaseResponse>

    @GET("/schedules/{scheduleID}/details")
    fun getDetailMemo(@Path("scheduleID")scheduleID: Int):Call<DetailMemoResponse>


}