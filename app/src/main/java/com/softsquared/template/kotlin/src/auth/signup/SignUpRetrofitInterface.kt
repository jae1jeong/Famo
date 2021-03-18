package com.softsquared.template.kotlin.src.auth.signup

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.auth.signup.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SignUpRetrofitInterface {

    @POST("users/sign-up")
    fun postSignUp(@Body signUpRequest:PostRequestSignUp): Call<SignUpResponse>

    @POST("users/phone")
    fun postSendMessage(@Body sendMessageRequest:PostRequestSendMessage):Call<BaseResponse>

    @POST("users/phone/auth")
    fun getCheckAuthNumber(@Body getRequestCheckAuthNumber: GetRequestCheckAuthNumber):Call<CheckAuthNumberResponse>
}