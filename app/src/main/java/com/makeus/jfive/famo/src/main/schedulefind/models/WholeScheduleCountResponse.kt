package com.makeus.jfive.famo.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.config.BaseResponse

data class WholeScheduleCountResponse(
    @SerializedName("totaldata") val totaldata: ArrayList<WholeScheduleTotalCountDataArrayList>,
    @SerializedName("totaldonedata") val totaldonedata: ArrayList<WholeScheduleTotalCountDataArrayList>
): BaseResponse()
