package com.softsquared.template.kotlin.src.auth.loginInformation

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.auth.loginInformation.models.KakaoLoginResponse
import com.softsquared.template.kotlin.src.auth.loginInformation.models.PatchKakaoLoginNumberRequest
import com.softsquared.template.kotlin.src.auth.loginInformation.models.PostKakaoLoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginInformationService(val view: LoginInformationView) {

    fun tryGetKakaoLogin(postKakaoLoginRequest : PostKakaoLoginRequest){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(LoginInformationRetrofitInterface::class.java)

        homeRetrofitInterface.postKakaoLogin(postKakaoLoginRequest).enqueue(object :
            Callback<KakaoLoginResponse> {
            override fun onResponse(call: Call<KakaoLoginResponse>, response : Response<KakaoLoginResponse>) {
//                Log.d("값 확인", "tryGetMyPage body:  ${kakaoLoginResponse.body()}")
//                Log.d("값 확인", "tryGetMyPage code:  ${kakaoLoginResponse.code()}")
                view.onPostKakaoLoginSuccess(response.body() as KakaoLoginResponse)
            }

            override fun onFailure(call: Call<KakaoLoginResponse>, t: Throwable) {
                                Log.d("통신실패", "tryGetKakaoLogin:  ")
                view.onPostKakaoLoginFail(t.message ?: "통신 오류")
            }
        })
    }

    fun tryPatchKakaoLoginNumber(patchKakaoLoginNumberRequest: PatchKakaoLoginNumberRequest){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(LoginInformationRetrofitInterface::class.java)

        homeRetrofitInterface.patchKakaoLoginNumber(patchKakaoLoginNumberRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response : Response<BaseResponse>) {
//                Log.d("값 확인", "tryGetMyPage body:  ${kakaoLoginResponse.body()}")
//                Log.d("값 확인", "tryGetMyPage code:  ${kakaoLoginResponse.code()}")
                view.onPatchKakaoLoginNumberSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("통신실패", "tryGetKakaoLogin:  ")
                view.onPatchKakaoLoginNumberFail(t.message ?: "통신 오류")
            }
        })
    }
}