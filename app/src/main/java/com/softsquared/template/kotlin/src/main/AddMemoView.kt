package com.softsquared.template.kotlin.src.main

import com.softsquared.template.kotlin.config.BaseResponse

interface AddMemoView {
    fun onPostAddMemoSuccess(response:BaseResponse)
    fun onPostAddMemoFailure(message:String)
}