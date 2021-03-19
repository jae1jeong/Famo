package com.softsquared.template.kotlin.src.auth.kakaologin

import com.softsquared.template.kotlin.config.BaseResponse

interface KakaoLoginNumberView {

    //카카오 첫 로그인 시 번호입력
    fun onPatchKakaoLoginNumberSuccess(response : BaseResponse)
    fun onPatchKakaoLoginNumberFail(message: String)
}