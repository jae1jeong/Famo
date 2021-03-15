package com.softsquared.template.kotlin.src.auth.loginInformation.models

import com.google.gson.annotations.SerializedName

data class PostKakaoLoginRequest(
    @SerializedName("kakaoAccessToken") val kakaoAccessToken: String,
    @SerializedName("kakaoRefreshToken") val kakaoRefreshToken: String
)
