package com.softsquared.template.kotlin.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class CategoryInquiryResponse(

    @SerializedName("data") val data: ArrayList<CategoryInquiryDataArrayList>
) : BaseResponse()
