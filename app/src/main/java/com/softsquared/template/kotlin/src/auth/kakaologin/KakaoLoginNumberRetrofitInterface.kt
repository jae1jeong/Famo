package com.softsquared.template.kotlin.src.auth.kakaologin

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.auth.kakaologin.models.PatchKakaoLoginNumberRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH

interface KakaoLoginNumberRetrofitInterface {

    @PATCH("users/phone")
    fun patchKakaoLoginNumber(@Body patchKakaoLoginNumberRequest : PatchKakaoLoginNumberRequest)
            : Call<BaseResponse>
}