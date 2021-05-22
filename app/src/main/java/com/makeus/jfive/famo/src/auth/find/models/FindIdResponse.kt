package com.makeus.jfive.famo.src.auth.find.models

import com.makeus.jfive.famo.config.BaseResponse

data class FindIdResponse(
        val userID:Int,
        val loginID:String
):BaseResponse()
