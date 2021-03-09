package com.softsquared.template.kotlin.src.main.today

import com.softsquared.template.kotlin.config.ApplicationClass
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
}