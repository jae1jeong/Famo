package com.softsquared.template.kotlin.src.main.today.models

data class MemoItem (
    val id: Int,
    val createAtMonth:String,
    val createAtDay:Int,
    val title:String,
    val description:String,
    val isChecked:Boolean,
    val colorState:String
        )