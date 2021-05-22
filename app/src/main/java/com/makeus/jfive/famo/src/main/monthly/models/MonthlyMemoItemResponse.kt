package com.makeus.jfive.famo.src.main.monthly.models

import com.google.gson.JsonArray
import com.makeus.jfive.famo.config.BaseResponse

data class MonthlyMemoItemResponse (
        val data:JsonArray
        ):BaseResponse()