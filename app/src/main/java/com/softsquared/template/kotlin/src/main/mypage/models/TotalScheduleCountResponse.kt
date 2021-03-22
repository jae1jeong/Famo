package com.softsquared.template.kotlin.src.main.mypage.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class TotalScheduleCountResponse(
    @SerializedName("totaldata") val totaldata: ArrayList<TotalScheduleCountDataArrayList>,
    @SerializedName("totaldonedata") val totaldonedata: ArrayList<TotaldoneScheduleCountDataArrayList>
): BaseResponse()
