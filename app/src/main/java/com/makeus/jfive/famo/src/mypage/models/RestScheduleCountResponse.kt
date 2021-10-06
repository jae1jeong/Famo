package com.makeus.jfive.famo.src.mypage.models

import com.google.gson.JsonArray
import com.makeus.jfive.famo.config.BaseResponse

data class RestScheduleCountResponse(
        val data:JsonArray
):BaseResponse()