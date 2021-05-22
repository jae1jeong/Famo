package com.makeus.jfive.famo.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.config.BaseResponse

data class CategoryInquiryResponse(

    @SerializedName("data") val data: ArrayList<CategoryInquiryDataArrayList>
) : BaseResponse()
