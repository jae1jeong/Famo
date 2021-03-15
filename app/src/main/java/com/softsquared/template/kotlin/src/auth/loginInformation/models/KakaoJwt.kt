package com.softsquared.template.kotlin.src.auth.loginInformation.models

import com.google.gson.annotations.SerializedName

data class KakaoJwt(
    @SerializedName("x-access-token") val x_access_token: String
)
