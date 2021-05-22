package com.makeus.jfive.famo.src.main.today.models

import com.google.gson.JsonArray
import com.makeus.jfive.famo.config.BaseResponse

data class ScheduleItemsResponse (
        val data:JsonArray
        ):BaseResponse()