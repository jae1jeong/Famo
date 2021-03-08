package com.softsquared.template.kotlin.src.auth.signup

import com.softsquared.template.kotlin.src.auth.signup.models.SignUpResponse

interface SignUpView {
    fun onPostSignUpSuccess(signUpResponse: SignUpResponse)
    fun onPostSignUpFailure(message:String)
}