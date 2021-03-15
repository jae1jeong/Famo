package com.softsquared.template.kotlin.src.main.today

import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodayService(val view:TodayView){

    fun onGetScheduleItems(){
        val todayRetrofitInterface = ApplicationClass.sRetrofit.create(TodayRetrofitInterface::class.java)
        todayRetrofitInterface.getTodayMemos().enqueue(object: Callback<ScheduleItemsResponse>{
            override fun onResponse(call: Call<ScheduleItemsResponse>, response: Response<ScheduleItemsResponse>) {
                view.onGetScheduleItemsSuccess(response.body() as ScheduleItemsResponse)
            }

            override fun onFailure(call: Call<ScheduleItemsResponse>, t: Throwable) {
                view.onGetScheduleItemsFailure(t.message ?: "스케쥴 일정 조회 관련 통신 오류")
            }

        })
    }

    fun onPutDeleteMemo(scheduleID:Int){
        val todayRetrofitInterface = ApplicationClass.sRetrofit.create(TodayRetrofitInterface::class.java)
        todayRetrofitInterface.deleteMemo(scheduleID).enqueue(object:Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onDeleteMemoSuccess(response.body() as BaseResponse,scheduleID)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onDeleteMemoFailure(t.message ?: "메모 삭제 관련 통신 오류")
            }

        })
    }

    fun onPostCheckItem(scheduleID: Int){
        val todayRetrofitInterface = ApplicationClass.sRetrofit.create(TodayRetrofitInterface::class.java)
        todayRetrofitInterface.postItemCheck(scheduleID).enqueue(object:Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPostItemCheckSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostItemCheckFailure(t.message ?: "메모 일정 체크 관련 통신 오류")
            }

        })

    }
}