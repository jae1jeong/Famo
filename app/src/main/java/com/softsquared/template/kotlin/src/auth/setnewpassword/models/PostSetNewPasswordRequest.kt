package com.softsquared.template.kotlin.src.auth.setnewpassword.models

import com.google.gson.annotations.SerializedName

data class PostSetNewPasswordRequest(
    @SerializedName("loginID") val loginID:String,
    @SerializedName("password") val password:String
)