package com.makeus.jfive.famo.src.domain.use_case.get_top_comment

import com.makeus.jfive.famo.src.domain.model.today.TopComment
import com.makeus.jfive.famo.src.domain.repository.TodayRepository
import io.reactivex.Single
import javax.inject.Inject

class GetTopCommentUseCase @Inject constructor(
    private val todayRepo:TodayRepository
) {
    fun execute(): Single<TopComment> {
        return todayRepo.getTopComment()
    }
}