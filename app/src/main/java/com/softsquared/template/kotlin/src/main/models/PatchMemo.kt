package com.softsquared.template.kotlin.src.main.models

data class PatchMemo (
    val scheduleName:String,
    val scheduleDate:String?,
    val scheduleCategoryID:Int?,
    val scheduleMemo:String
    )