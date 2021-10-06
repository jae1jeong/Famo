package com.makeus.jfive.famo.src.data.api

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.domain.model.today.PostMemoCheck
import com.makeus.jfive.famo.src.domain.model.today.TodayMemoList
import com.makeus.jfive.famo.src.domain.model.today.TopComment
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse
import com.makeus.jfive.famo.src.main.today.models.ChangePositionItemRequest
import com.makeus.jfive.famo.src.main.today.models.CheckItemRequest
import com.makeus.jfive.famo.src.main.today.models.ScheduleItemsResponse
import com.makeus.jfive.famo.src.main.today.models.TopCommentResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface TodayApiInterface {

    @GET("schedules/today")
    fun getTodayMemos(): Single<TodayMemoList>

    @PUT("schedules/{scheduleID}")
    fun deleteMemo(@Path("scheduleID") scheduleID:Int):  Single<BaseResponse>

    @POST("schedules/achievements/today")
    fun postItemCheck(@Body postMemoCheck: PostMemoCheck): Observable<BaseResponse>

    @GET("profiles/comments")
    fun getTopComment(): Single<TopComment>

    @POST("schedules/orderchanges")
    fun postChangeItemPosition(@Body scheduleID: Int): Single<BaseResponse>

    @GET("/schedules/{scheduleID}/details")
    fun getDetailMemo(@Path("scheduleID")scheduleID: Int): Single<DetailMemoResponse>
}