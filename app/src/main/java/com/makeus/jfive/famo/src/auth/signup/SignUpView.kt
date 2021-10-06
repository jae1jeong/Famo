package com.makeus.jfive.famo.src.auth.signup

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.auth.signup.models.CheckAuthNumberResponse
import com.makeus.jfive.famo.src.auth.signup.models.SignUpResponse

interface SignUpView {
    fun onPostSignUpSuccess(signUpResponse: SignUpResponse)
    fun onPostSignUpFailure(message:String)
    fun onPostSendMessageSuccess(response:BaseResponse)
    fun onPostSendMessageFailure(message: String)
    fun onGetCheckAuthNumberSuccess(response:CheckAuthNumberResponse)
    fun onGetCheckAuthNumberFailure(message: String)

}