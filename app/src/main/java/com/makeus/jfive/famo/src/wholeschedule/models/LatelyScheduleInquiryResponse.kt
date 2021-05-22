package com.makeus.jfive.famo.src.wholeschedule.models

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.config.BaseResponse

data class LatelyScheduleInquiryResponse(
    @SerializedName("data") val data: ArrayList<LatelyScheduleInquiryDataArrayList>
) : BaseResponse()