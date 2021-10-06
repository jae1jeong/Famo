package com.makeus.jfive.famo.src.domain.model.today


import com.google.gson.annotations.SerializedName

data class TopComment(
    @SerializedName("Dday")
    val dday: Int,
    @SerializedName("goalDate")
    val goalDate: String?,
    @SerializedName("goalStatus")
    val goalStatus: Int,
    @SerializedName("goalTitle")
    val goalTitle: String?,
    @SerializedName("nickname")
    val nickname:String?,
    @SerializedName("titleComment")
    val titleComment:String?
)