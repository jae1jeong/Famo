package com.makeus.jfive.famo.src.domain.model.month


import com.google.gson.annotations.SerializedName

data class MonthlyList(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("result")
    val result: List<Result>
)