package com.softsquared.template.kotlin.src.main.models

data class PostTodayRequestAddMemo (
        val scheduleName : String,
        val scheduleMemo: String,
        val scheduleCategoryID:Int
        )