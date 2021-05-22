package com.makeus.jfive.famo.src.auth.kakaologin

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.auth.kakaologin.models.PatchKakaoLoginNumberRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH

interface KakaoLoginNumberRetrofitInterface {

    @PATCH("users/phone")
    fun patchKakaoLoginNumber(@Body patchKakaoLoginNumberRequest : PatchKakaoLoginNumberRequest)
            : Call<BaseResponse>
}