package com.softsquared.template.kotlin.src.auth.signup

import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.auth.signup.models.PostRequestSignUp
import com.softsquared.template.kotlin.src.auth.signup.models.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpService(val view:SignUpView) {

    fun tryPostSignUp(signUpRequest:PostRequestSignUp){
        val signUpRetrofitInterface = ApplicationClass.sRetrofit.create(SignUpRetrofitInterface::class.java)
        signUpRetrofitInterface.postSignUp(signUpRequest).enqueue(object: Callback<SignUpResponse>{
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                view.onPostSignUpSuccess(response.body() as SignUpResponse)
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                view.onPostSignUpFailure(t.message ?: "회원가입 관련 통신 오류")
            }

        })
    }

}