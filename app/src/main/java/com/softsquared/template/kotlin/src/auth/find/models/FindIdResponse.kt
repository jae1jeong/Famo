package com.softsquared.template.kotlin.src.auth.find.models

import com.softsquared.template.kotlin.config.BaseResponse

data class FindIdResponse(
        val userID:Int,
        val loginID:String
):BaseResponse()
