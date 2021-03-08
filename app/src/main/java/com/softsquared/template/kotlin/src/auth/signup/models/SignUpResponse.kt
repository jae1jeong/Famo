package com.softsquared.template.kotlin.src.auth.signup.models

import com.softsquared.template.kotlin.config.BaseResponse

data class SignUpResponse (
    private val jwt:String,
    val nickname:String
        ):BaseResponse()