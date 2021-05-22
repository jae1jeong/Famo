package com.makeus.jfive.famo.src.mypageedit.models

import com.google.gson.annotations.SerializedName

data class PutMyPageUpdateRequest(
    @SerializedName("nickname") val nickname : String,
    @SerializedName("titleComment") val titleComment : String,
    @SerializedName("goalStatus") val goalStatus : Int,
    @SerializedName("goalTitle") val goalTitle : String,
    @SerializedName("goalDate") val goalDate : String
)
