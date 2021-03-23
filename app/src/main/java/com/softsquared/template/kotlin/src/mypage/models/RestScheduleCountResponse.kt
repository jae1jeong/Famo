package com.softsquared.template.kotlin.src.mypage.models

import com.softsquared.template.kotlin.config.BaseResponse

data class RestScheduleCountResponse(
        val remainScheduleCount:Int
):BaseResponse()