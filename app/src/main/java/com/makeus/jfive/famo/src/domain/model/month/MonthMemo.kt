package com.makeus.jfive.famo.src.domain.model.month


import com.google.gson.annotations.SerializedName

data class MonthMemo(
    @SerializedName("categoryID")
    val categoryID: Int,
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("colorInfo")
    val colorInfo: String,
    @SerializedName("scheduleDate")
    val scheduleDate: String,
    @SerializedName("scheduleFormDate")
    val scheduleFormDate: String,
    @SerializedName("scheduleID")
    val scheduleID: Int,
    @SerializedName("scheduleMemo")
    var scheduleMemo: String,
    @SerializedName("scheduleName")
    var scheduleName: String,
    @SerializedName("scheduleOrder")
    val scheduleOrder: Int,
    val isChecked:Boolean = false
)