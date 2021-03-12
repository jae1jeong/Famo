package com.softsquared.template.kotlin.src.main

import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.models.PostTodayRequestAddMemo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMemoService(val view: AddMemoView) {

    fun tryPostAddMemo(postTodayRequestAddMemo: PostTodayRequestAddMemo){
        val addMemoRetrofitInterface = ApplicationClass.sRetrofit.create(AddMemoRetrofitInterface::class.java)
        addMemoRetrofitInterface.postAddMemo(postTodayRequestAddMemo).enqueue(object: Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPostAddMemoSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostAddMemoFailure(t.message ?: "일정 생성 관련 통신 오류")
            }

        })
    }
}