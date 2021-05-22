package com.makeus.jfive.famo.src.auth.login

import com.makeus.jfive.famo.src.auth.login.models.LoginResponse
import com.makeus.jfive.famo.src.auth.login.models.PostRequestLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRetrofitInterface {
    @POST("users/sign-in")
    fun postLogin(@Body loginRequest:PostRequestLogin ): Call<LoginResponse>

}