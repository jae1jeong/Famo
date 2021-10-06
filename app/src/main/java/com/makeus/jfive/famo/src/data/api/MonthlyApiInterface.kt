package com.makeus.jfive.famo.src.data.api

import com.makeus.jfive.famo.src.domain.model.month.MonthByDate
import com.makeus.jfive.famo.src.domain.model.month.MonthlyList
import com.makeus.jfive.famo.src.main.monthly.models.AllMemoResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MonthlyApiInterface {
    @GET("/schedules/dates")
    fun getMemoByDate(@Query("scheduleDate") scheduleDate:String): Observable<MonthByDate>

    @GET("/schedules")
    fun getAllMemos(): Single<AllMemoResponse>

    @GET("/schedules/months")
    fun getUserMonthlyDateList(@Query("month") month:Int, @Query("year") year:Int): Single<MonthlyList>
}