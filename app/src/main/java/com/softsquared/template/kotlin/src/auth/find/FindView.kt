package com.softsquared.template.kotlin.src.auth.find

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.auth.find.models.FindIdResponse

interface FindView {
    fun onGetFindIdSuccess(response:FindIdResponse)
    fun onGetFindIdFailure(message:String)
    fun onGetCompareSuccess(response:BaseResponse)
    fun onGetCompareFailure(message: String)
}