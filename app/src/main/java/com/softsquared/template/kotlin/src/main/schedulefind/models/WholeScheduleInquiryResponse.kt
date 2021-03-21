package com.softsquared.template.kotlin.src.main.schedulefind.models

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class WholeScheduleInquiryResponse(
    @SerializedName("data") val data: ArrayList<WholeScheduleInquiryDataArrayList>
) : BaseResponse()