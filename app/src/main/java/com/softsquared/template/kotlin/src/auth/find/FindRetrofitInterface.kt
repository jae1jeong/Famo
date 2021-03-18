package com.softsquared.template.kotlin.src.auth.find

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.auth.find.models.FindIdResponse
import com.softsquared.template.kotlin.src.auth.find.models.GetCompareIdPhoneNumber
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FindRetrofitInterface {

    @GET("users/login-id")
    fun getFindId(@Header("otp-auth-token")token:String): Call<FindIdResponse>

    @GET("users/phone")
    fun getCompareIdPhoneNumber(@Query("loginid") loginId:String,@Header("otp-auth-token") otpAuthToken:String):Call<BaseResponse>
}