package com.makeus.jfive.famo.src.auth.loginInformation.models

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.config.BaseResponse

data class KakaoLoginResponse(
    @SerializedName("userID") val userID: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("jwt") val jwt: String,
) : BaseResponse()
