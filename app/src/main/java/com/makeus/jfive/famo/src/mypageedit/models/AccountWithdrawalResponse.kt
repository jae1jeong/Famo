package com.makeus.jfive.famo.src.mypageedit.models

import com.google.gson.annotations.SerializedName
import com.makeus.jfive.famo.config.BaseResponse

data class AccountWithdrawalResponse(

    @SerializedName("userID") val userID : Int
    
): BaseResponse()
