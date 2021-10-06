package com.makeus.jfive.famo.src.data.repository

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.data.api.TodayApiInterface
import com.makeus.jfive.famo.src.domain.model.today.PostMemoCheck
import com.makeus.jfive.famo.src.domain.model.today.TodayMemoList
import com.makeus.jfive.famo.src.domain.model.today.TopComment
import com.makeus.jfive.famo.src.domain.repository.TodayRepository
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse
import com.makeus.jfive.famo.src.main.today.models.ScheduleItemsResponse
import com.makeus.jfive.famo.src.main.today.models.TopCommentResponse
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class TodayRepositoryImpl @Inject constructor(
    private val api:TodayApiInterface
):TodayRepository {
    override fun getTodayMemos(): Single<TodayMemoList> {
        return api.getTodayMemos()
    }

    override fun deleteMemo(scheduleID: Int): Single<BaseResponse> {
        return api.deleteMemo(scheduleID)
    }

    override fun postItemCheck(postMemoCheck: PostMemoCheck): Observable<BaseResponse> {
        return api.postItemCheck(postMemoCheck)
    }

    override fun getTopComment(): Single<TopComment> {
        return api.getTopComment()
    }

    override fun postChangeItemPosition(scheduleID: Int): Single<BaseResponse> {
        return api.postChangeItemPosition(scheduleID)
    }

    override fun getDetailMemo(scheduleID: Int): Single<DetailMemoResponse> {
        return api.getDetailMemo(scheduleID)
    }
}