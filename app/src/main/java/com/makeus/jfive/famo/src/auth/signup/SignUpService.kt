package com.makeus.jfive.famo.src.auth.signup

import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.auth.signup.models.*
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

    fun tryPostSendMessage(sendMessageRequest: PostRequestSendMessage){
        val signUpRetrofitInterface = ApplicationClass.sRetrofit.create(SignUpRetrofitInterface::class.java)
        signUpRetrofitInterface.postSendMessage(sendMessageRequest).enqueue(object:Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPostSendMessageSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostSendMessageFailure(t.message ?: "메시지 전송 관련 통신 오류")
            }

        })
    }

    fun tryGetCheckAuthNumber(getRequestCheckAuthNumber: GetRequestCheckAuthNumber){
        val signUpRetrofitInterface = ApplicationClass.sRetrofit.create(SignUpRetrofitInterface::class.java)
        signUpRetrofitInterface.getCheckAuthNumber(getRequestCheckAuthNumber).enqueue(object:Callback<CheckAuthNumberResponse>{
            override fun onResponse(call: Call<CheckAuthNumberResponse>, response: Response<CheckAuthNumberResponse>) {
                view.onGetCheckAuthNumberSuccess(response.body() as CheckAuthNumberResponse)
            }

            override fun onFailure(call: Call<CheckAuthNumberResponse>, t: Throwable) {
                view.onGetCheckAuthNumberFailure(t.message ?: "인증 번호 확인 관련 통신 오류")
            }

        })
    }

}