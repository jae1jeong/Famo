package com.makeus.jfive.famo.src.auth.loginInformation

import com.makeus.jfive.famo.src.auth.loginInformation.models.KakaoLoginResponse
import com.makeus.jfive.famo.src.auth.loginInformation.models.PostKakaoLoginRequest
import retrofit2.Call
import retrofit2.http.*

interface LoginInformationRetrofitInterface {

    //카카오 로그인
    @POST("users/kakao")
    fun postKakaoLogin(@Body postKakaoLoginRequest : PostKakaoLoginRequest)
        : Call<KakaoLoginResponse>

}