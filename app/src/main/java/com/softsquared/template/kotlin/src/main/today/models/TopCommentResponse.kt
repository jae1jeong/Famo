package com.softsquared.template.kotlin.src.main.today.models

import com.softsquared.template.kotlin.config.BaseResponse

data class TopCommentResponse (
        val nickname:String,
        val titleComment:String,
        val goalStatus:Int,
        val goalTitle:String,
        val Dday:Int,
        val goalDate:String,
        ):BaseResponse()