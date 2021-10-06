package com.makeus.jfive.famo.src.auth.kakaologin.models

import com.google.gson.annotations.SerializedName

data class PatchKakaoLoginNumberRequest(
    @SerializedName("phoneNumber") val phoneNumber: String,
)
