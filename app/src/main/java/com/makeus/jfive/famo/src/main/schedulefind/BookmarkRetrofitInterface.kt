package com.makeus.jfive.famo.src.main.schedulefind

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BookmarkRetrofitInterface {

    //즐겨찾기on/off
    @POST("schedules/picks")
    fun postBookmark(@Body bookmarkRequest: BookmarkRequest) : Call<BaseResponse>

}