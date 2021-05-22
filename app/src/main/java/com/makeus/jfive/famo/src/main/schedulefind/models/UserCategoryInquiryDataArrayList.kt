package com.makeus.jfive.famo.src.main.schedulefind.models

import com.google.gson.annotations.SerializedName

data class UserCategoryInquiryDataArrayList(

    @SerializedName("categoryID") val categoryID: Int,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("colorInfo") val colorInfo: String

)
