package com.softsquared.template.kotlin.src.main.schedulefind.models

import java.util.*

data class CategoryScheduleInquiryData(val id : Int,
                                       var date : String,
                                       var name : String,
                                       var memo : String,
                                       val pick: Int,
                                       var color : String)
