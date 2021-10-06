package com.makeus.jfive.famo.src.domain.use_case.delete_memo

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.domain.repository.TodayRepository
import io.reactivex.Single
import javax.inject.Inject

class DeleteMemoUseCase @Inject constructor(
    private val todayRepository: TodayRepository
) {
    fun execute(scheduleId: Int): Single<BaseResponse> = todayRepository.deleteMemo(scheduleId)

}