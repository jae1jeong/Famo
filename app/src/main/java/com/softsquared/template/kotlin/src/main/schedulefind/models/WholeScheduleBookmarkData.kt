package com.softsquared.template.kotlin.src.main.schedulefind.models

data class WholeScheduleBookmarkData(val scheduleID : Int,
                                     val scheduleDate : String,
                                     var scheduleName : String,
                                     var scheduleMemo : String,
                                     val schedulePick : Int,
                                     var categoryID : Int,
                                     var colorInfo : String?)
