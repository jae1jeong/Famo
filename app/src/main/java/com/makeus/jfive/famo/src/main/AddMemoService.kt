package com.makeus.jfive.famo.src.main

import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse
import com.makeus.jfive.famo.src.main.models.PatchMemo
import com.makeus.jfive.famo.src.main.models.PostTodayRequestAddMemo
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
    fun tryPatchMemo(scheduleID:Int,patchMemo: PatchMemo){
        val addMemoRetrofitInterface = ApplicationClass.sRetrofit.create(AddMemoRetrofitInterface::class.java)
        addMemoRetrofitInterface.patchMemo(scheduleID,patchMemo).enqueue(object: Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPatchMemoSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPatchMemoFailure(t.message ?: "일정 수정 관련 통신 오류")
            }

        })

    }

    fun tryGetDetailMemo(scheduleID: Int){
        val addMemoRetrofitInterface = ApplicationClass.sRetrofit.create(AddMemoRetrofitInterface::class.java)
        addMemoRetrofitInterface.getDetailMemo(scheduleID).enqueue(object:Callback<DetailMemoResponse>{
            override fun onResponse(call: Call<DetailMemoResponse>, response: Response<DetailMemoResponse>) {
                view.onGetDetailMemoSuccess(response.body() as DetailMemoResponse)
            }

            override fun onFailure(call: Call<DetailMemoResponse>, t: Throwable) {
                view.onGetDetailMemoFailure(t.message ?: "일정 상세 조회 관련 통신 오류")
            }

        })

    }
}