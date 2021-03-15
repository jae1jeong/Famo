package com.softsquared.template.kotlin.src.auth.loginInformation

import com.softsquared.template.kotlin.src.auth.loginInformation.models.KakaoLoginResponse
import retrofit2.Call
import retrofit2.http.GET

interface LoginInformationRetrofitInterface {

    //카카오 로그인
    @GET("users/kakao")
    fun getKakaoLogin() : Call<KakaoLoginResponse>
}