package com.makeus.jfive.famo.src.auth.check_token

import com.makeus.jfive.famo.config.BaseResponse
import retrofit2.Call
import retrofit2.http.GET

interface TokenCheckRetrofitInterface {
    @GET("/users/check")
    fun getCheckToken():Call<BaseResponse>
}