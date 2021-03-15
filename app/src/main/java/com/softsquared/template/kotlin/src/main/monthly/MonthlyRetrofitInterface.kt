package com.softsquared.template.kotlin.src.main.monthly

import com.softsquared.template.kotlin.src.main.monthly.models.AllMemoResponse
import com.softsquared.template.kotlin.src.main.monthly.models.MonthlyMemoItemResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MonthlyRetrofitInterface {

    @GET("/schedules/dates")
    fun getMonthlyMemos(@Query("scheduleDate") scheduleDate:String): Call<MonthlyMemoItemResponse>

    @GET("/schedules")
    fun getAllMemos():Call<AllMemoResponse>
}