package com.softsquared.template.kotlin.src.main.today.models

import com.google.gson.annotations.SerializedName

data class ChangePositionItemRequest (
     @SerializedName("scheduleID") val scheduleID:Int,
     @SerializedName("scheduleOrder") val scheduleOrder:Int
        )