package com.makeus.jfive.famo.src.domain.model.month


import com.google.gson.annotations.SerializedName

data class MonthByDate(
    @SerializedName("data")
    val data: List<MonthMemo>
)