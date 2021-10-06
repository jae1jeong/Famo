package com.makeus.jfive.famo.src.domain.use_case.get_detail_memo

import com.makeus.jfive.famo.src.domain.model.main.DetailMemoDto
import com.makeus.jfive.famo.src.domain.repository.MainRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetDetailMemoUseCase @Inject constructor(
    private val mainRepo:MainRepository
) {
    fun execute(scheduleId:Int):Observable<DetailMemoDto> {
        return mainRepo.getDetailMemo(scheduleId)
    }
}