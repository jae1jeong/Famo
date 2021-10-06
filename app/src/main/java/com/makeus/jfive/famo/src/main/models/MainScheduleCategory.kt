package com.makeus.jfive.famo.src.main.models

import com.google.gson.annotations.SerializedName

data class MainScheduleCategory(
    @SerializedName("categoryID")
    val id: Int,
    @SerializedName("categoryName")
    val text: String,
    @SerializedName("colorInfo")
    val color: String?,
    var selected: Boolean = false
)