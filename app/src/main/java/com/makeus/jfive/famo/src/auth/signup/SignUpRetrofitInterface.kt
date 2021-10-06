package com.makeus.jfive.famo.src.auth.signup

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.auth.signup.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpRetrofitInterface {

    @POST("users/sign-up")
    fun postSignUp(@Body signUpRequest:PostRequestSignUp): Call<SignUpResponse>

    @POST("users/phone")
    fun postSendMessage(@Body sendMessageRequest:PostRequestSendMessage):Call<BaseResponse>

    @POST("users/phone/auth")
    fun getCheckAuthNumber(@Body getRequestCheckAuthNumber: GetRequestCheckAuthNumber):Call<CheckAuthNumberResponse>
}