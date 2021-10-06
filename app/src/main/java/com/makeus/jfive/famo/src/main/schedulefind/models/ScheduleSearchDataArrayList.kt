package com.makeus.jfive.famo.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName

data class ScheduleSearchDataArrayList(

    @SerializedName("scheduleID") val scheduleID: Int,
    @SerializedName("scheduleName") val scheduleName: String,
    @SerializedName("scheduleMemo") val scheduleMemo: String,
    @SerializedName("scheduleDate") val scheduleDate: String,
    @SerializedName("schedulePick") val schedulePick: Int,
    @SerializedName("colorInfo") val colorInfo: String
)
