@file:JvmName("TodayMemoKt")

package com.makeus.jfive.famo.src.domain.model.today


import com.google.gson.annotations.SerializedName

data class TodayMemoDto(
    @SerializedName("categoryID")
    val categoryID: Int?,
    @SerializedName("categoryName")
    val categoryName: String?,
    @SerializedName("colorInfo")
    val colorInfo: String?,
    @SerializedName("scheduleDate")
    val scheduleDate: String,
    @SerializedName("scheduleFormDate")
    val scheduleFormDate: String,
    @SerializedName("scheduleID")
    val scheduleID: Int,
    @SerializedName("scheduleMemo")
    val scheduleMemo: String,
    @SerializedName("scheduleName")
    val scheduleName: String,
    @SerializedName("scheduleOrder")
    val scheduleOrder: Int,
    @SerializedName("schedulePick")
    val schedulePick: Int,
    @SerializedName("scheduleStatus")
    var scheduleStatus: Int
)


fun TodayMemoDto.toTodayMemo(): TodayMemo {
    val splitMonthAndDay = this.scheduleDate.split(" ")
    val month = splitMonthAndDay.get(0)
    val day = splitMonthAndDay.get(1)
    var pick = true
    if (schedulePick == -1) {
        pick = false
    }
    var status = true
    if (scheduleStatus == -1) {
        status = false
    }
    return TodayMemo(
        categoryID = categoryID,
        categoryName = categoryName,
        colorInfo = colorInfo,
        scheduleMonth = month,
        scheduleDay = day,
        scheduleFormDate = scheduleFormDate,
        scheduleID = scheduleID,
        scheduleMemo = scheduleMemo,
        scheduleName = scheduleName,
        scheduleOrder = scheduleOrder,
        schedulePick = pick,
        scheduleStatus = status
    )
}