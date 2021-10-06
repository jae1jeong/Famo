package com.makeus.jfive.famo.src.domain.use_case.get_memo_by_date

import com.makeus.jfive.famo.src.domain.model.month.MonthByDate
import com.makeus.jfive.famo.src.domain.repository.MonthlyRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetMemoByDateUseCase @Inject constructor(
    private val monthlyRepository:MonthlyRepository
) {

    fun execute(date:String): Observable<MonthByDate> {
        return monthlyRepository.getMemoByDate(date)
    }
}