package com.softsquared.template.kotlin.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName

data class WholeScheduleInquiryDataArrayList(

    @SerializedName("scheduleID") val scheduleID: Int,
    @SerializedName("scheduleDate") val scheduleDate: String,
    @SerializedName("scheduleName") val scheduleName: String,
    @SerializedName("scheduleMemo") val scheduleMemo: String,
    @SerializedName("schedulePick") val schedulePick: Int,
    @SerializedName("scheduleStatus") val scheduleStatus: Int,
    @SerializedName("colorInfo") val colorInfo: String

)
