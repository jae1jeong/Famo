package com.makeus.jfive.famo.src.main.today.models

import com.makeus.jfive.famo.config.BaseResponse

data class TopCommentResponse (
        val nickname:String,
        val titleComment:String,
        val goalStatus:Int,
        val goalTitle:String,
        val Dday:Int,
        val goalDate:String,
        ):BaseResponse()