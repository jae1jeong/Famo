package com.makeus.jfive.famo.src.searchhistories.models

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.config.BaseResponse

data class SearchHistoriesResponse(
    @SerializedName("data") val data: ArrayList<SearchHistoriesDataList>
) : BaseResponse()
