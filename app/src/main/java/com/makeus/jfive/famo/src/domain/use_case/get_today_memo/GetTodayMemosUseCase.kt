package com.makeus.jfive.famo.src.domain.use_case.get_today_memo

import com.makeus.jfive.famo.src.domain.model.today.TodayMemoList
import com.makeus.jfive.famo.src.domain.repository.TodayRepository
import io.reactivex.Single
import javax.inject.Inject

class GetTodayMemosUseCase @Inject constructor(
    private val todayRepository: TodayRepository
) {
    fun execute():Single<TodayMemoList>{
        return todayRepository.getTodayMemos()
    }
}