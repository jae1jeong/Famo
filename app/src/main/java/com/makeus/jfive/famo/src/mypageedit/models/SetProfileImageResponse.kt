package com.makeus.jfive.famo.src.mypageedit.models

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.config.BaseResponse

data class SetProfileImageResponse (
        @SerializedName("userID") val userID:Int,
        @SerializedName("profileImageURL") val profileImageURL:String
        ):BaseResponse()