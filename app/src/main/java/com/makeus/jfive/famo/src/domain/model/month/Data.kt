package com.makeus.jfive.famo.src.domain.model.month


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("colorInfo")
    val colorInfo: String,
    @SerializedName("scheduleDate")
    val scheduleDate: String,
    @SerializedName("scheduleForm")
    val scheduleForm: String,
    @SerializedName("scheduleID")
    val scheduleID: Int,
    @SerializedName("scheduleMemo")
    val scheduleMemo: String,
    @SerializedName("scheduleName")
    val scheduleName: String,
    @SerializedName("scheduleOrder")
    val scheduleOrder: Int
)