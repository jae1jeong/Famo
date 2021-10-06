package com.makeus.jfive.famo.src.domain.model.today


import com.google.gson.annotations.SerializedName

data class TodayMemoList(
    @SerializedName("data")
    val todayMemoDtoList: List<TodayMemoDto>
)

fun List<TodayMemoDto>.mapperToTodayMemo():List<TodayMemo>{
    return this.map{
        it.toTodayMemo()
    }
}