package com.softsquared.template.kotlin.src.main.monthly

import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.main.monthly.models.AllMemoResponse
import com.softsquared.template.kotlin.src.main.monthly.models.MonthlyMemoItemResponse
import com.softsquared.template.kotlin.src.main.monthly.models.MonthlyUserDateListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonthlyService(val view:MonthlyView) {

    fun onGetMonthlyMemoItems(scheduleDate:String){
        val monthlyRetrofitInterface = ApplicationClass.sRetrofit.create(MonthlyRetrofitInterface::class.java)
        monthlyRetrofitInterface.getMonthlyMemos(scheduleDate).enqueue(object: Callback<MonthlyMemoItemResponse>{
            override fun onResponse(call: Call<MonthlyMemoItemResponse>, response: Response<MonthlyMemoItemResponse>) {
                view.onGetMonthlyMemoItemSuccess(response.body() as MonthlyMemoItemResponse)
            }

            override fun onFailure(call: Call<MonthlyMemoItemResponse>, t: Throwable) {
                view.onGetMonthlyMemoItemFailure(t.message ?: "월간 메모 일정 관련 통신 오류")
            }

        })
    }

    fun onGetAllMemos(){
        val monthlyRetrofitInterface = ApplicationClass.sRetrofit.create(MonthlyRetrofitInterface::class.java)
        monthlyRetrofitInterface.getAllMemos().enqueue(object:Callback<AllMemoResponse>{
            override fun onResponse(call: Call<AllMemoResponse>, response: Response<AllMemoResponse>) {
                view.onGetAllMemosSuccess(response.body() as AllMemoResponse)
            }

            override fun onFailure(call: Call<AllMemoResponse>, t: Throwable) {
                view.onGetAllMemosFailure(t.message ?: "전체 일정 조회 관련 통신 오류")
            }

        })
    }

    fun onGetUserDateList(month:Int,year:Int){
        val monthlyRetrofitInterface = ApplicationClass.sRetrofit.create(MonthlyRetrofitInterface::class.java)
        monthlyRetrofitInterface.getUserMonthlyDateList(month,year).enqueue(object:Callback<MonthlyUserDateListResponse>{
            override fun onResponse(call: Call<MonthlyUserDateListResponse>, response: Response<MonthlyUserDateListResponse>) {
                view.onGetUserDateListSuccess(response.body() as MonthlyUserDateListResponse)
            }

            override fun onFailure(call: Call<MonthlyUserDateListResponse>, t: Throwable) {
                view.onGetUserDateListFailure(t.message ?: "월별 일정 조회 관련 통신 오류")
            }

        })
    }
}