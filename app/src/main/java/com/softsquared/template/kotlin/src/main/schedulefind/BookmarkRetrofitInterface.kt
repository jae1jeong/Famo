package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BookmarkRetrofitInterface {

    //즐겨찾기on/off
    @POST("schedules/picks")
    fun postBookmark(@Body bookmarkRequest: BookmarkRequest) : Call<BaseResponse>

}