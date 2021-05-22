package com.makeus.jfive.famo.src.auth.kakaologin

import android.util.Log
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.auth.kakaologin.models.PatchKakaoLoginNumberRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoLoginNumberService(val view : KakaoLoginNumberView) {

    fun tryPatchKakaoLoginNumber(patchKakaoLoginNumberRequest: PatchKakaoLoginNumberRequest){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(
            KakaoLoginNumberRetrofitInterface::class.java)

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