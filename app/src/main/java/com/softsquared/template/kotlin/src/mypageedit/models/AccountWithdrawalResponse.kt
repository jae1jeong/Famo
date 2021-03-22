package com.softsquared.template.kotlin.src.mypageedit.models

import com.google.gson.annotations.SerializedName
import com.softsquared.template.kotlin.config.BaseResponse

data class AccountWithdrawalResponse(

    @SerializedName("userID") val userID : Int
    
): BaseResponse()
