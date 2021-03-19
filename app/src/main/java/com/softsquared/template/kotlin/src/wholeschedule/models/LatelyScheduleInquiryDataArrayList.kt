package com.softsquared.template.kotlin.src.wholeschedule.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryDataArrayList
import java.util.*

data class LatelyScheduleInquiryDataArrayList(

    @SerializedName("scheduleID") val scheduleID: Int,
    @SerializedName("scheduleDate") val scheduleDate: Date,
    @SerializedName("scheduleName") val scheduleName: String,
    @SerializedName("scheduleMemo") val scheduleMemo: String,
    @SerializedName("schedulePick") val schedulePick: Int,
    @SerializedName("categoryID") val categoryID: Int,
    @SerializedName("colorInfo") val colorInfo: String
)