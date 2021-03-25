package com.softsquared.template.kotlin.src.searchhistories.models

import com.google.gson.annotations.SerializedName

data class SearchHistoriesDataList(
    @SerializedName("searchHistory") val searchHistory: String
)
