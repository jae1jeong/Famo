package com.makeus.jfive.famo.src.main.today.models

data class MemoItem (
    val id: Int,
    val createAtMonth:String,
    val createAtDay:Int,
    var title:String,
    var description:String?,
    var isChecked:Boolean,
    val colorState:String?,
    var formDateStr:String?,
    var scheduleOrder:Int
        )