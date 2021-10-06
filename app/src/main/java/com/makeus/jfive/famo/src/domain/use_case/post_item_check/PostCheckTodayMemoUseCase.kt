package com.makeus.jfive.famo.src.domain.use_case.post_item_check

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.domain.model.today.PostMemoCheck
import com.makeus.jfive.famo.src.domain.repository.TodayRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject


class PostCheckTodayMemoUseCase @Inject constructor(
    private val todayRepo:TodayRepository
){
    fun execute(postMemoCheck:PostMemoCheck): Observable<BaseResponse>{
        return todayRepo.postItemCheck(postMemoCheck)
    }
}