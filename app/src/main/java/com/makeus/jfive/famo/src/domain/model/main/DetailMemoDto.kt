package com.makeus.jfive.famo.src.domain.model.main

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.src.domain.model.month.MonthMemo

data class DetailMemoDto(
    @SerializedName("data")
    val detailMemoDto: List<DetailMemo>
)
