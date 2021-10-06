package com.makeus.jfive.famo.src.domain.use_case.get_monthly_date_list

import com.makeus.jfive.famo.src.domain.model.month.MonthlyList
import com.makeus.jfive.famo.src.domain.repository.MonthlyRepository
import io.reactivex.Single
import javax.inject.Inject

class GetMonthlyDateListUseCase @Inject constructor(
    private val monthlyRepository: MonthlyRepository
) {
    fun execute(month:Int,year:Int): Single<MonthlyList> {
        return monthlyRepository.getUserMonthlyDateList(month,year)
    }
}