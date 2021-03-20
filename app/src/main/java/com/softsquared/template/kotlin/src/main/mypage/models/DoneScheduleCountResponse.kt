package com.softsquared.template.kotlin.src.main.mypage.models

import com.google.gson.JsonArray
import com.softsquared.template.kotlin.config.BaseResponse

data class DoneScheduleCountResponse (
        val totaldata:JsonArray,
    val totaldonedata:JsonArray
        ):BaseResponse()