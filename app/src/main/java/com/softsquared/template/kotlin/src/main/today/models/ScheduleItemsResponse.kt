package com.softsquared.template.kotlin.src.main.today.models

import com.google.gson.JsonArray
import com.softsquared.template.kotlin.config.BaseResponse

data class ScheduleItemsResponse (
        val data:JsonArray
        ):BaseResponse()