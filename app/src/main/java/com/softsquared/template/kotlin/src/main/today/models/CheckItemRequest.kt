package com.softsquared.template.kotlin.src.main.today.models

import com.google.gson.annotations.SerializedName

data class CheckItemRequest(
      @SerializedName("scheduleID")  val scheduleID:Int
)