package com.makeus.jfive.famo.src.auth.signup.models

import com.makeus.jfive.famo.config.BaseResponse

data class CheckAuthNumberResponse (
        val jwt:String
        ): BaseResponse()