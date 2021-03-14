package com.softsquared.template.kotlin.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName

data class CategoryInquiryDataArrayList(

    @SerializedName("categoryID") val categoryID: Int,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("colorInfo") val colorInfo: String

)
