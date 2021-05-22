package com.makeus.jfive.famo.src.auth.loginInformation.models

import com.google.gson.annotations.SerializedName

data class KakaoJwt(
    @SerializedName("x-access-token") val x_access_token: String
)
