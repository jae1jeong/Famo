package com.softsquared.template.kotlin.src.auth.loginInformation

import com.softsquared.template.kotlin.src.auth.loginInformation.models.KakaoLoginResponse

interface LoginInformationView {

    //카카오 로그인
    fun onGetKakaoLoginSuccess(kakaoLoginResponse: KakaoLoginResponse)
    fun onGetKakaoLoginFail(message: String)
}