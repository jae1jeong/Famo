package com.softsquared.template.kotlin.src.main.schedulefind.models

data class ScheduleWholeData(val id : Int,
                             var date : String,
                             var name : String,
                             var memo : String?,
                             var pick: Int,
                             val status : Int,
                             var color : String?)
