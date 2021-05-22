package com.makeus.jfive.famo.src.auth.find.models

import com.google.gson.annotations.SerializedName

data class GetCompareIdPhoneNumber(
       @SerializedName("loginid") val loginId:String,
)