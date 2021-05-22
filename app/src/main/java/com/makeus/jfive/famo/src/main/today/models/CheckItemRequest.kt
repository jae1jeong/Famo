package com.makeus.jfive.famo.src.main.today.models

import com.google.gson.annotations.SerializedName

data class CheckItemRequest(
      @SerializedName("scheduleID")  val scheduleID:Int
)