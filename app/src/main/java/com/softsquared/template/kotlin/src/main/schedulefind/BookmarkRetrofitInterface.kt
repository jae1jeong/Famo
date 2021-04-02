package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BookmarkRetrofitInterface {

    //즐겨찾기on/off
    @POST("schedules/picks")
    fun postBookmark(@Body bookmarkRequest: BookmarkRequest) : Call<BaseResponse>

}