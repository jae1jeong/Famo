package com.softsquared.template.kotlin.src.mypage.models

import com.google.gson.JsonArray
import com.softsquared.template.kotlin.config.BaseResponse

data class RestScheduleCountResponse(
        val data:JsonArray
):BaseResponse()