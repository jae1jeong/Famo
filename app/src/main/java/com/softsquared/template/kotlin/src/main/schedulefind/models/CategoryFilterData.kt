package com.softsquared.template.kotlin.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName

data class CategoryFilterData(val scheduleID : Int,
                              val scheduleDate : String,
                              val scheduleName :String,
                              val scheduleMemo :String,
                              val schedulePick :Int,
                              val colorInfo :String)

