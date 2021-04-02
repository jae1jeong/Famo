package com.softsquared.template.kotlin.src.auth.login

import com.softsquared.template.kotlin.src.auth.login.models.LoginResponse

interface LoginView {

    fun onPostLoginSuccess(response: LoginResponse)
    fun onPostLoginFailure(message:String)
}