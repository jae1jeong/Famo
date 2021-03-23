package com.softsquared.template.kotlin.src.main.schedulefind.models

import java.util.*

data class WholeScheduleLatelyData(val scheduleID : Int,
                                   val scheduleDate : Date,
                                   val scheduleName : String,
                                   val scheduleMemo : String,
                                   val schedulePick : Int,
                                   val categoryID : Int,
                                   val colorInfo : String)