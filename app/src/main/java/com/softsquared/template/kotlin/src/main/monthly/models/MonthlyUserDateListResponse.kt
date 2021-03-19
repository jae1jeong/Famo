package com.softsquared.template.kotlin.src.main.monthly.models

import com.google.gson.JsonArray
import com.softsquared.template.kotlin.config.BaseResponse

data class MonthlyUserDateListResponse (
    val data:JsonArray,
    val result:JsonArray
):BaseResponse()