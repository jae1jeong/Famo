package com.makeus.jfive.famo.src.auth.signup.models

import com.google.gson.annotations.SerializedName

data class GetRequestCheckAuthNumber (
        @SerializedName("phoneNumber") val phoneNumber:String,
        @SerializedName("authCode") val authCode:String
        )