package com.makeus.jfive.famo.src.domain.use_case.patch_memo

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.domain.model.main.PatchMemo
import com.makeus.jfive.famo.src.domain.repository.MainRepository
import io.reactivex.Single
import javax.inject.Inject

class PatchMemoUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    fun execute(scheduleId:Int,patchMemo:PatchMemo): Single<BaseResponse>{
        return mainRepository.patchMemo(scheduleId,patchMemo)
    }
}