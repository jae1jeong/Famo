package com.softsquared.template.kotlin.src.auth.signup

import com.softsquared.template.kotlin.src.auth.signup.models.PostRequestSignUp
import com.softsquared.template.kotlin.src.auth.signup.models.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpRetrofitInterface {

    @POST("users/sign-up")
    fun postSignUp(@Body signUpRequest:PostRequestSignUp): Call<SignUpResponse>
}