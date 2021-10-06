package com.makeus.jfive.famo.src.domain.model.main

data class PostTodayRequestAddMemo (
        val scheduleName : String,
        val scheduleMemo: String,
        val scheduleCategoryID:Int?,
        val scheduleDate:String?
        )