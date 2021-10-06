package com.makeus.jfive.famo.src.auth.login.models

import com.makeus.jfive.famo.config.BaseResponse

data class LoginResponse (
        val nickname: String,
        val jwt : String,
        val userID:Int
        ):BaseResponse()
