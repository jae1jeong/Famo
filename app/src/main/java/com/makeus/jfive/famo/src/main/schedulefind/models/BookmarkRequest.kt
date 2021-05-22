package com.makeus.jfive.famo.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName

data class BookmarkRequest(

    @SerializedName("scheduleID") val scheduleID: Int
)
