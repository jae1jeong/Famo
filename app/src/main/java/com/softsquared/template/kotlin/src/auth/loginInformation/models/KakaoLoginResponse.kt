package com.softsquared.template.kotlin.src.auth.loginInformation.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class KakaoLoginResponse(
    @SerializedName("userID") val userID: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("jwt") val jwt: String,
) : BaseResponse()
