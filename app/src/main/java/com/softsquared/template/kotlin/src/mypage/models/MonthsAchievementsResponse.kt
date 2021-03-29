package com.softsquared.template.kotlin.src.mypage.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse
import java.util.*
import kotlin.collections.ArrayList

data class MonthsAchievementsResponse(
    @SerializedName("data") val data : JsonObject
) : BaseResponse()
