package com.makeus.jfive.famo.src.domain.use_case.post_add_memo

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.domain.model.main.PostTodayRequestAddMemo
import com.makeus.jfive.famo.src.domain.repository.MainRepository
import io.reactivex.Single
import javax.inject.Inject

class PostAddMemoUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    fun execute(memoRequest: PostTodayRequestAddMemo): Single<BaseResponse> {
        return mainRepository.postAddMemo(memoRequest)
    }
}