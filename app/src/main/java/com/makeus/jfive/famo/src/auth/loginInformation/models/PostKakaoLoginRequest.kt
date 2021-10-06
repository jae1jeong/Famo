package com.makeus.jfive.famo.src.auth.loginInformation.models

import com.google.gson.annotations.SerializedName

data class PostKakaoLoginRequest(
    @SerializedName("kakaoAccessToken") val kakaoAccessToken: String,
    @SerializedName("kakaoRefreshToken") val kakaoRefreshToken: String
)
