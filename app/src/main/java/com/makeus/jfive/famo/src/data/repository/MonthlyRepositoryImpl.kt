package com.makeus.jfive.famo.src.data.repository

import com.makeus.jfive.famo.src.data.api.MonthlyApiInterface
import com.makeus.jfive.famo.src.domain.model.month.MonthByDate
import com.makeus.jfive.famo.src.domain.model.month.MonthlyList
import com.makeus.jfive.famo.src.domain.repository.MonthlyRepository
import com.makeus.jfive.famo.src.main.monthly.models.AllMemoResponse
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class MonthlyRepositoryImpl @Inject constructor(
    private val api:MonthlyApiInterface
):MonthlyRepository {
    override fun getMemoByDate(scheduleDate: String): Observable<MonthByDate> {
        return api.getMemoByDate(scheduleDate)
    }

    override fun getAllMemos(): Single<AllMemoResponse> {
        return api.getAllMemos()
    }

    override fun getUserMonthlyDateList(month: Int, year: Int): Single<MonthlyList> {
        return api.getUserMonthlyDateList(month, year)
    }
}