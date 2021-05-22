package com.makeus.jfive.famo.src.main.category.models

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.config.BaseResponse

data class CategoryInsertResponse(
    @SerializedName("data") val data : String
) : BaseResponse()
