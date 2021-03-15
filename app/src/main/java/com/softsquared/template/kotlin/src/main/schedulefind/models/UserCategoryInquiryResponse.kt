package com.softsquared.template.kotlin.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class UserCategoryInquiryResponse(

    @SerializedName("data") val data: ArrayList<UserCategoryInquiryDataArrayList>
) : BaseResponse()
