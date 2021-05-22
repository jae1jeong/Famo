package com.makeus.jfive.famo.src.main.models

data class PostTodayRequestAddMemo (
        val scheduleName : String,
        val scheduleMemo: String,
        val scheduleCategoryID:Int?,
        val scheduleDate:String?
        )