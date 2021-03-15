package com.softsquared.template.kotlin.src.auth.signup.models

import com.softsquared.template.kotlin.config.BaseResponse

data class CheckAuthNumberResponse (
        val jwt:String
        ): BaseResponse()