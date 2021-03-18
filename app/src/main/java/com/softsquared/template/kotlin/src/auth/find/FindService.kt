package com.softsquared.template.kotlin.src.auth.find

import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.auth.find.models.FindIdResponse
import com.softsquared.template.kotlin.src.auth.find.models.GetCompareIdPhoneNumber
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindService(val view:FindView) {

    fun tryGetFindId(otpToken:String){
        val findRetrofitInterface = ApplicationClass.sRetrofit.create(FindRetrofitInterface::class.java)
        findRetrofitInterface.getFindId(otpToken).enqueue(object: Callback<FindIdResponse>{
            override fun onResponse(call: Call<FindIdResponse>, response: Response<FindIdResponse>) {
                view.onGetFindIdSuccess(response.body() as FindIdResponse)
            }
            override fun onFailure(call: Call<FindIdResponse>, t: Throwable) {
                view.onGetFindIdFailure(t.message ?: "아이디 찾기 관련 통신 오류")
            }
        })
    }
    fun tryGetCompareIdPhoneNumber(loginId: String,otpAuthToken:String){
        val findRetrofitInterface = ApplicationClass.sRetrofit.create(FindRetrofitInterface::class.java)
        findRetrofitInterface.getCompareIdPhoneNumber(loginId,otpAuthToken).enqueue(object:Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onGetCompareSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onGetCompareFailure(t.message ?: "아이디 휴대폰 번호 일치 관련 통신 실패")
            }

        })
    }
}