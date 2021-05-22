package com.makeus.jfive.famo.src.mypage.models

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.config.BaseResponse

data class TotalScheduleCountResponse(
    @SerializedName("totaldata") val totaldata: ArrayList<TotalScheduleCountDataArrayList>,
    @SerializedName("totaldonedata") val totaldonedata: ArrayList<TotaldoneScheduleCountDataArrayList>
): BaseResponse()
