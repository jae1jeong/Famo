package com.makeus.jfive.famo.src.data.api

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.domain.model.main.*
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.CategoryInquiryResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.UserCategoryInquiryResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface MainApiInterface {

    // 남은 일정 수 조회(전체/오늘)
    @GET("schedules/left-over")
    fun getRestScheduleCount(@Query("filter") date:String): Observable<RemainScheduleCountDto>

    // 해낸 일정 수 조회
    @GET("schedules/counts")
    fun getDoneScheduleCount():Observable<DoneScheduleCountDto>

    @POST("/schedules")
    fun postAddMemo(@Body addMemoRequest: PostTodayRequestAddMemo): Single<BaseResponse>

    @PATCH("/schedules/{scheduleID}")
    fun patchMemo(@Path("scheduleID")scheduleID :Int, @Body patchMemo: PatchMemo): Single<BaseResponse>

    @GET("/schedules/{scheduleID}/details")
    fun getDetailMemo(@Path("scheduleID")scheduleID: Int): Observable<DetailMemoDto>

    //유져별 카테고리 조회
    @GET("categories")
    fun getUserCategoryInquiry() : Observable<CategoryDto>

    //일정별 카테고리조회
    @GET("category-schedules")
    fun getCategoryInquiry(@Query("scheduleCategoryID") scheduleCategoryID : Int,
                           @Query("offset") offset : Int,
                           @Query("limit") limit : Int) : Observable<CategoryInquiryResponse>

}