package com.makeus.jfive.famo.src.main

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse
import com.makeus.jfive.famo.src.main.models.PatchMemo
import com.makeus.jfive.famo.src.main.models.PostTodayRequestAddMemo
import retrofit2.Call
import retrofit2.http.*

interface AddMemoRetrofitInterface {

    @POST("/schedules")
    fun postAddMemo(@Body addMemoRequest: PostTodayRequestAddMemo): Call<BaseResponse>

    @PATCH("/schedules/{scheduleID}")
    fun patchMemo(@Path("scheduleID")scheduleID :Int,@Body patchMemo: PatchMemo):Call<BaseResponse>

    @GET("/schedules/{scheduleID}/details")
    fun getDetailMemo(@Path("scheduleID")scheduleID: Int):Call<DetailMemoResponse>
}