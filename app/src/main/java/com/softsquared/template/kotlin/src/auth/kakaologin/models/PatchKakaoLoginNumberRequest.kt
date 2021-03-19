package com.softsquared.template.kotlin.src.auth.kakaologin.models

import com.google.gson.annotations.SerializedName

data class PatchKakaoLoginNumberRequest(
    @SerializedName("phoneNumber") val phoneNumber: String,
)
