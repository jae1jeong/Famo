package com.makeus.jfive.famo.src.domain.model.main


import com.google.gson.annotations.SerializedName

data class RemainScheduleCountDto(
    @SerializedName("data")
    val `data`: List<RemainScheduleCount>
)