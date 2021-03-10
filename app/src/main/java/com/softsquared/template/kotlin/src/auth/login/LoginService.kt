package com.softsquared.template.kotlin.src.auth.login

import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.auth.login.models.LoginResponse
import com.softsquared.template.kotlin.src.auth.login.models.PostRequestLogin
import com.softsquared.template.kotlin.src.auth.signup.models.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginService(val view:LoginView) {

    fun tryPostLogin(loginRequestLogin: PostRequestLogin){
        val loginRetrofitInterface = ApplicationClass.sRetrofit.create(LoginRetrofitInterface::class.java)
        loginRetrofitInterface.postLogin(loginRequestLogin).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                view.onPostLoginSuccess(response.body() as LoginResponse)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                view.onPostLoginFailure(t.message ?: "로그인 관련 통신 오류")
            }

        })
    }
}