package com.makeus.jfive.famo.src.mypage.models

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.config.BaseResponse

data class MyPageResponse(
    @SerializedName("loginID") val loginID : String,
    @SerializedName("loginMethod") val loginMethod : String,
    @SerializedName("nickname") val nickname : String,
    @SerializedName("profileImageURL") val profileImageURL : String,
    @SerializedName("titleComment") val titleComment : String,
    @SerializedName("goalStatus") val goalStatus : Int,
    @SerializedName("goalTitle") val goalTitle : String,
    @SerializedName("Dday") val Dday : Int,
    @SerializedName("goalDate") val goalDate : String
):BaseResponse()
