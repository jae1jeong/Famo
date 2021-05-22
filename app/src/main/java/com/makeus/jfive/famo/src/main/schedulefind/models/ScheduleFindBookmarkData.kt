package com.makeus.jfive.famo.src.main.schedulefind.models

data class ScheduleFindBookmarkData(val scheduleID : Int,
                                    val scheduleDate : String,
                                    val scheduleName : String,
                                    val scheduleMemo : String,
                                    val schedulePick : Int,
                                    val categoryID : Int,
                                    val colorInfo : String)
