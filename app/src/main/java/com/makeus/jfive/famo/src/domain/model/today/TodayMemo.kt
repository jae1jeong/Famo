package com.makeus.jfive.famo.src.domain.model.today

data class TodayMemo
    (
    val categoryID: Int?,
    val categoryName: String?,
    val colorInfo: String?,
    val scheduleMonth: String,
    val scheduleDay:String,
    val scheduleFormDate: String,
    val scheduleID: Int,
    val scheduleMemo: String,
    val scheduleName: String,
    val scheduleOrder: Int,
    val schedulePick: Boolean,
    var scheduleStatus: Boolean
    )

