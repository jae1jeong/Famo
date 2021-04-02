package com.softsquared.template.kotlin.src.main.schedulefind.models

import java.util.*

data class ScheduleSearchData(val scheduleID : Int,
                              val scheduleName : String,
                              val scheduleMemo : String,
                              val scheduleDate : String,
                              var schedulePick: Int,
                              val colorInfo : String)
