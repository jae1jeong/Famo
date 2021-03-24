package com.softsquared.template.kotlin.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class ScheduleFindBookmarkData(val scheduleID : Int,
                                    val scheduleDate : String,
                                    val scheduleName : String,
                                    val scheduleMemo : String,
                                    val schedulePick : Int,
                                    val categoryID : Int,
                                    val colorInfo : String)