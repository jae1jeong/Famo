package com.softsquared.template.kotlin.src.auth.signup

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.auth.signup.models.CheckAuthNumberResponse
import com.softsquared.template.kotlin.src.auth.signup.models.SignUpResponse

interface SignUpView {
    fun onPostSignUpSuccess(signUpResponse: SignUpResponse)
    fun onPostSignUpFailure(message:String)
    fun onPostSendMessageSuccess(response:BaseResponse)
    fun onPostSendMessageFailure(message: String)
    fun onGetCheckAuthNumberSuccess(response:CheckAuthNumberResponse)
    fun onGetCheckAuthNumberFailure(message: String)

}