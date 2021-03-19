package com.softsquared.template.kotlin.src.auth.setnewpassword

import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.auth.setnewpassword.models.PostSetNewPasswordRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetNewPasswordService(val view:SetNewPasswordView) {

    fun trySetNewPassword(otpAuthToken:String,postSetNewPasswordRequest: PostSetNewPasswordRequest){
        val setNewPasswordRetrofitInterface = ApplicationClass.sRetrofit.create(SetNewPasswordRetrofitInterface::class.java)
        setNewPasswordRetrofitInterface.postSetPassword(otpAuthToken,postSetNewPasswordRequest).enqueue(object: Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPostSetNewPasswordSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostSetNewPasswordFailure(t.message ?: "비밀번호 재설정 관련 통신 오류")
            }

        })
    }
}

