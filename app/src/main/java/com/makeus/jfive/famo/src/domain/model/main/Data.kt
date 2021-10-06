package com.makeus.jfive.famo.src.domain.model.main


import com.google.gson.annotations.SerializedName

data class DetailMemo(
    @SerializedName("categoryName")
    val categoryName: String?,
    @SerializedName("colorInfo")
    val colorInfo: String?,
    @SerializedName("scheduleDate")
    val scheduleDate: String,
    @SerializedName("scheduleForm")
    val scheduleForm: String,
    @SerializedName("scheduleMemo")
    val scheduleMemo: String?,
    @SerializedName("scheduleName")
    val scheduleName: String?,
    @SerializedName("scheduleTime")
    val scheduleTime: Any
)