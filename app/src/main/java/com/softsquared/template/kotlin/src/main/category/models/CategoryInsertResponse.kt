package com.softsquared.template.kotlin.src.main.category.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class CategoryInsertResponse(
    @SerializedName("data") val data : String
) : BaseResponse()
