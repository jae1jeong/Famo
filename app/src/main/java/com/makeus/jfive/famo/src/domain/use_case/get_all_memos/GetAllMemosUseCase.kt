package com.makeus.jfive.famo.src.domain.use_case.get_all_memos

import com.makeus.jfive.famo.src.domain.repository.MonthlyRepository
import com.makeus.jfive.famo.src.main.monthly.models.AllMemoResponse
import io.reactivex.Single
import javax.inject.Inject

class GetAllMemosUseCase @Inject constructor(
    private val monthlyRepository: MonthlyRepository
) {
    fun execute():Single<AllMemoResponse>{
        return monthlyRepository.getAllMemos()
    }
}