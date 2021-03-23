package com.softsquared.template.kotlin.src.mypageedit.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class PutMyPageUpdateRequest(
    @SerializedName("nickname") val nickname : String,
    @SerializedName("titleComment") val titleComment : String,
    @SerializedName("goalStatus") val goalStatus : Int,
    @SerializedName("goalTitle") val goalTitle : String,
    @SerializedName("goalDate") val goalDate : String
)
