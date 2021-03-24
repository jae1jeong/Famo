package com.softsquared.template.kotlin.src.mypage.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class MonthsAchievementsResponse(
    @SerializedName("data") val data : List<String>
) : BaseResponse()
