package com.softsquared.template.kotlin.src.main.schedulefind.models

data class CategoryFilterData(val scheduleID : Int,
                              val scheduleDate : String,
                              val scheduleName :String,
                              val scheduleMemo :String,
                              var schedulePick :Int,
                              val colorInfo :String)

