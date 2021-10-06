package com.makeus.jfive.famo.src.auth.find

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.auth.find.models.FindIdResponse

interface FindView {
    fun onGetFindIdSuccess(response:FindIdResponse)
    fun onGetFindIdFailure(message:String)
    fun onGetCompareSuccess(response:BaseResponse)
    fun onGetCompareFailure(message: String)
}