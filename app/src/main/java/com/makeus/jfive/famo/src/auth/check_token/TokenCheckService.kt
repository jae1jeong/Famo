package com.makeus.jfive.famo.src.auth.check_token

import android.util.Log
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenCheckService(val tokenCheckView: TokenCheckView) {

    fun tryCheckToken(){
        val tokenCheckRetrofitInterface = ApplicationClass.sRetrofit.create(TokenCheckRetrofitInterface::class.java)
        tokenCheckRetrofitInterface.getCheckToken().enqueue(object: Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if(response.body()?.isSuccess == true){
                    tokenCheckView.onTokenCheckSuccess()

                }else{
                    tokenCheckView.onTokenCheckFailure()
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                tokenCheckView.onTokenCheckFailure()
            }

        })
    }
}