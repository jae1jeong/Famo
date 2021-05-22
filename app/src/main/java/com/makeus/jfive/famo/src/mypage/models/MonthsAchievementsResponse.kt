package com.makeus.jfive.famo.src.mypage.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.config.BaseResponse

data class MonthsAchievementsResponse(
    @SerializedName("data") val data : JsonObject
) : BaseResponse()
