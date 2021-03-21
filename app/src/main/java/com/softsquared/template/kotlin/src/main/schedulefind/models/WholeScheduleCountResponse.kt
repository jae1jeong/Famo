package com.softsquared.template.kotlin.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class WholeScheduleCountResponse(
    @SerializedName("totaldata") val totaldata: ArrayList<WholeScheduleTotalCountDataArrayList>,
    @SerializedName("totaldonedata") val totaldonedata: ArrayList<WholeScheduleTotalCountDataArrayList>
): BaseResponse()
