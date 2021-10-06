package com.makeus.jfive.famo.src.main.today.models

import com.google.gson.annotations.SerializedName

data class ChangePositionItemRequest (
     @SerializedName("scheduleID") val scheduleID:Int,
     @SerializedName("scheduleOrder") val scheduleOrder:Int
        )