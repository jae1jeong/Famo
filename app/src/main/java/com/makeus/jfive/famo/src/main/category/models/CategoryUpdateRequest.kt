package com.makeus.jfive.famo.src.main.category.models

import com.google.gson.annotations.SerializedName

data class CategoryUpdateRequest(
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("categoryColor") val categoryColor: Int
)
