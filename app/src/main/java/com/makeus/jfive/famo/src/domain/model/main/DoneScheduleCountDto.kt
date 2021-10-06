package com.makeus.jfive.famo.src.domain.model.main

import com.google.gson.annotations.SerializedName

data class DoneScheduleCountDto(
    @SerializedName("totaldonedata")
    val doneScheduleCountDto:List<DoneScheduleCount>,
    @SerializedName("totaldata")
    val totalScheduleCount:List<TotalScheduleCount>
)
