package com.softsquared.template.kotlin.src.mypage.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class MyPageResponse(
    @SerializedName("loginID") val loginID : String,
    @SerializedName("loginMethod") val loginMethod : String,
    @SerializedName("nickname") val nickname : String,
    @SerializedName("profileImageURL") val profileImageURL : String,
    @SerializedName("titleComment") val titleComment : String,
    @SerializedName("goalStatus") val goalStatus : String,
    @SerializedName("goalTitle") val goalTitle : String,
    @SerializedName("Dday") val Dday : String,
    @SerializedName("goalDate") val goalDate : String
):BaseResponse()
