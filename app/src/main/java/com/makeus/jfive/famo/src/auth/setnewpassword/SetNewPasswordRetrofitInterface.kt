package com.makeus.jfive.famo.src.auth.setnewpassword

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.auth.setnewpassword.models.PostSetNewPasswordRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SetNewPasswordRetrofitInterface {
    @POST("users/password")
    fun postSetPassword(@Header("otp-auth-token")otpAuthToken:String,@Body postSetNewPasswordRequest: PostSetNewPasswordRequest): Call<BaseResponse>

}