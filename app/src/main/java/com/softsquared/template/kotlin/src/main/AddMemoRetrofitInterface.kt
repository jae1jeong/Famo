package com.softsquared.template.kotlin.src.main

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.models.PostTodayRequestAddMemo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AddMemoRetrofitInterface {

    @POST("/schedules")
    fun postAddMemo(@Body addMemoRequest:PostTodayRequestAddMemo): Call<BaseResponse>
}