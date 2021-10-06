package com.makeus.jfive.famo.src.domain.use_case.get_remain_schedule_count

import com.makeus.jfive.famo.src.domain.model.main.RemainScheduleCountDto
import com.makeus.jfive.famo.src.domain.repository.MainRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetRemainScheduleCountUseCase @Inject constructor(
    private val mainRepo:MainRepository
) {
    fun execute(date:String): Observable<RemainScheduleCountDto> {
        return mainRepo.getRestScheduleCount(date)
    }
}