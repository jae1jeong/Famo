package com.makeus.jfive.famo.src.auth.signup.models

import com.makeus.jfive.famo.config.BaseResponse

data class SignUpResponse (
    private val jwt:String,
    val nickname:String
        ):BaseResponse()