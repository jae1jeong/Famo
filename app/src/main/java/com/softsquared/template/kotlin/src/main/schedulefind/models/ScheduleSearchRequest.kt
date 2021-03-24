package com.softsquared.template.kotlin.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName

data class ScheduleSearchRequest(
    @SerializedName("searchWord") val searchWord: String
)