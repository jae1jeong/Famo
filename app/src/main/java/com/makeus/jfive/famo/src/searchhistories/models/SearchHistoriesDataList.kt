package com.makeus.jfive.famo.src.searchhistories.models

import com.google.gson.annotations.SerializedName

data class SearchHistoriesDataList(
    @SerializedName("searchHistory") val searchHistory: String
)
