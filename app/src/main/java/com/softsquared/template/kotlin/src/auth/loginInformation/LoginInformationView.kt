package com.softsquared.template.kotlin.src.auth.loginInformation

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.auth.loginInformation.models.KakaoLoginResponse

interface LoginInformationView {

    //카카오 로그인
    fun onPostKakaoLoginSuccess(response : KakaoLoginResponse)
    fun onPostKakaoLoginFail(message: String)

}