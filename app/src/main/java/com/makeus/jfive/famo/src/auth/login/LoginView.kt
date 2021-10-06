package com.makeus.jfive.famo.src.auth.login

import com.makeus.jfive.famo.src.auth.login.models.LoginResponse

interface LoginView {

    fun onPostLoginSuccess(response: LoginResponse)
    fun onPostLoginFailure(message:String)
}