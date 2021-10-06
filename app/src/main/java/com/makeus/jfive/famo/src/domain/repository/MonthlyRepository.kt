package com.makeus.jfive.famo.src.domain.repository

import com.makeus.jfive.famo.src.domain.model.month.MonthByDate
import com.makeus.jfive.famo.src.domain.model.month.MonthlyList
import com.makeus.jfive.famo.src.main.monthly.models.AllMemoResponse
import io.reactivex.Observable
import io.reactivex.Single

interface MonthlyRepository {

    fun getMemoByDate(scheduleDate:String): Observable<MonthByDate>
    fun getAllMemos():Single<AllMemoResponse>
    fun getUserMonthlyDateList(month:Int,year:Int):Single<MonthlyList>
}
