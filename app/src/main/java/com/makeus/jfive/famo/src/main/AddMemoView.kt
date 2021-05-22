package com.makeus.jfive.famo.src.main

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse

interface AddMemoView {
    fun onPostAddMemoSuccess(response:BaseResponse)
    fun onPostAddMemoFailure(message:String)
    fun onPatchMemoSuccess(response: BaseResponse)
    fun onPatchMemoFailure(message: String)
    fun onGetDetailMemoSuccess(response:DetailMemoResponse)
    fun onGetDetailMemoFailure(message: String)
}