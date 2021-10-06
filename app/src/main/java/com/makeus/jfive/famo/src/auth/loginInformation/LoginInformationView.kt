package com.makeus.jfive.famo.src.auth.loginInformation

import com.makeus.jfive.famo.src.auth.loginInformation.models.KakaoLoginResponse

interface LoginInformationView {

    //카카오 로그인
    fun onPostKakaoLoginSuccess(response : KakaoLoginResponse)
    fun onPostKakaoLoginFail(message: String)

}