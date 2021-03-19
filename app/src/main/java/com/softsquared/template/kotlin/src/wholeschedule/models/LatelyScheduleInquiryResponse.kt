package com.softsquared.template.kotlin.src.wholeschedule.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class LatelyScheduleInquiryResponse(
    @SerializedName("data") val data: ArrayList<LatelyScheduleInquiryDataArrayList>
) : BaseResponse()