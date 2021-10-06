package com.makeus.jfive.famo.src.mypageedit.models

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.config.BaseResponse

data class MyPageEditRequest(
    @SerializedName("nickname") val nickname : String,
    @SerializedName("titleComment") val titleComment : String,
    @SerializedName("goalStatus") val goalStatus : Int
): BaseResponse()

