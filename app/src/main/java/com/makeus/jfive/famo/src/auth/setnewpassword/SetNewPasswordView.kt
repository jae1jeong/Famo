package com.makeus.jfive.famo.src.auth.setnewpassword

import com.makeus.jfive.famo.config.BaseResponse

interface SetNewPasswordView {
    fun onPostSetNewPasswordSuccess(response:BaseResponse)
    fun onPostSetNewPasswordFailure(message:String)
}