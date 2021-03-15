package com.softsquared.template.kotlin.src.main

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.models.DetailMemoResponse

interface AddMemoView {
    fun onPostAddMemoSuccess(response:BaseResponse)
    fun onPostAddMemoFailure(message:String)
    fun onPatchMemoSuccess(response: BaseResponse)
    fun onPatchMemoFailure(message: String)
    fun onGetDetailMemoSuccess(response:DetailMemoResponse)
    fun onGetDetailMemoFailure(message: String)
}