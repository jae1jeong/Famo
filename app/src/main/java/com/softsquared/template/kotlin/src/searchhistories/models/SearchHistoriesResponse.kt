package com.softsquared.template.kotlin.src.searchhistories.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class SearchHistoriesResponse(
    @SerializedName("data") val data: ArrayList<SearchHistoriesDataList>
) : BaseResponse()
