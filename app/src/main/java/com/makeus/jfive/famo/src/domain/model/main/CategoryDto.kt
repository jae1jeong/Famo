package com.makeus.jfive.famo.src.domain.model.main

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.src.main.models.MainScheduleCategory

data class CategoryDto(
    @SerializedName("data")
    val categoryDto: ArrayList<MainScheduleCategory>
)
