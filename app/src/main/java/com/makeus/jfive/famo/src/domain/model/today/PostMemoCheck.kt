package com.makeus.jfive.famo.src.domain.model.today

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body

data class PostMemoCheck(
    @SerializedName("scheduleID")
    val scheduleID:Int
)
