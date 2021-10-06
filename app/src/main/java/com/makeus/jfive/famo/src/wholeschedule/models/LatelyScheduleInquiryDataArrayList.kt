package com.makeus.jfive.famo.src.wholeschedule.models

import com.google.gson.annotations.SerializedName

data class LatelyScheduleInquiryDataArrayList(

    @SerializedName("scheduleID") val scheduleID: Int,
    @SerializedName("scheduleDate") val scheduleDate: String,
    @SerializedName("scheduleName") val scheduleName: String,
    @SerializedName("scheduleMemo") val scheduleMemo: String,
    @SerializedName("schedulePick") val schedulePick: Int,
    @SerializedName("categoryID") val categoryID: Int,
    @SerializedName("colorInfo") val colorInfo: String
)