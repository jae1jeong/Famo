package com.softsquared.template.kotlin.src.auth.login.models

import com.softsquared.template.kotlin.config.BaseResponse

data class LoginResponse (
        val nickname: String,
        val jwt : String,
        val userID:Int
        ):BaseResponse()
