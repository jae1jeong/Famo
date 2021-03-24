package com.softsquared.template.kotlin.src.main.schedulefind.models

import java.util.*

data class CategoryScheduleInquiryData(val id : Int,
                                       val date : String,
                                       val name : String,
                                       val memo : String,
                                       val pick: Int,
                                       val color : String)
