package com.softsquared.template.kotlin.src.mypageedit.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class MyPageEditRequest(
    @SerializedName("nickname") val nickname : String,
    @SerializedName("titleComment") val titleComment : String,
    @SerializedName("goalStatus") val goalStatus : Int
): BaseResponse()

