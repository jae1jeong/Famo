package com.makeus.jfive.famo.src.main.schedulefind.models

data class WholeScheduleLatelyData(val scheduleID : Int,
                                   val scheduleDate : String,
                                   var scheduleName : String,
                                   var scheduleMemo : String,
                                   val schedulePick : Int,
                                   val categoryID : Int,
                                   val colorInfo : String)