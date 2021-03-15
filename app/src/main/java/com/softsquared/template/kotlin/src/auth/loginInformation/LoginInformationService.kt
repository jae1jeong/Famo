package com.softsquared.template.kotlin.src.auth.loginInformation

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.auth.loginInformation.models.KakaoLoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginInformationService(val view: LoginInformationView) {

    fun tryGetKakaoLogin(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(LoginInformationRetrofitInterface::class.java)

        homeRetrofitInterface.getKakaoLogin().enqueue(object :
            Callback<KakaoLoginResponse> {
            override fun onResponse(call: Call<KakaoLoginResponse>, response : Response<KakaoLoginResponse>) {
//                Log.d("값 확인", "tryGetMyPage body:  ${kakaoLoginResponse.body()}")
//                Log.d("값 확인", "tryGetMyPage code:  ${kakaoLoginResponse.code()}")
                view.onGetKakaoLoginSuccess(response.body() as KakaoLoginResponse)
            }

            override fun onFailure(call: Call<KakaoLoginResponse>, t: Throwable) {
                                Log.d("통신실패", "tryGetKakaoLogin:  ")
                view.onGetKakaoLoginFail(t.message ?: "통신 오류")
            }
        })
    }
}