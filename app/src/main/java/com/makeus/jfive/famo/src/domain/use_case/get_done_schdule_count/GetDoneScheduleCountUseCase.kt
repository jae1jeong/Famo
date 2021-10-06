package com.makeus.jfive.famo.src.domain.use_case.get_done_schdule_count

import com.makeus.jfive.famo.src.domain.model.main.DoneScheduleCountDto
import com.makeus.jfive.famo.src.domain.repository.MainRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetDoneScheduleCountUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    fun execute(): Observable<DoneScheduleCountDto> {
        return mainRepository.getDoneScheduleCount()
    }
}