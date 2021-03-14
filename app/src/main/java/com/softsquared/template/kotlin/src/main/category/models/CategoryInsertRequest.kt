package com.softsquared.template.kotlin.src.main.category.models

import com.google.gson.annotations.SerializedName

data class CategoryInsertRequest(
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("categoryColor") val categoryColor: Int
)
