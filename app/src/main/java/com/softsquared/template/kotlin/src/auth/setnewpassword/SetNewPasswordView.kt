package com.softsquared.template.kotlin.src.auth.setnewpassword

import com.softsquared.template.kotlin.config.BaseResponse

interface SetNewPasswordView {
    fun onPostSetNewPasswordSuccess(response:BaseResponse)
    fun onPostSetNewPasswordFailure(message:String)
}