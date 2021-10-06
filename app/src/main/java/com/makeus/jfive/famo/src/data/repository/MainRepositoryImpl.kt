package com.makeus.jfive.famo.src.data.repository

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.data.api.MainApiInterface
import com.makeus.jfive.famo.src.domain.model.main.*
import com.makeus.jfive.famo.src.domain.repository.MainRepository
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.CategoryInquiryResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.UserCategoryInquiryResponse
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api:MainApiInterface
):MainRepository {
    override fun getRestScheduleCount(date: String): Observable<RemainScheduleCountDto> {
        return api.getRestScheduleCount(date)
    }

    override fun getDoneScheduleCount(): Observable<DoneScheduleCountDto> {
        return api.getDoneScheduleCount()
    }

    override fun postAddMemo(memoRequest: PostTodayRequestAddMemo): Single<BaseResponse> {
        return api.postAddMemo(memoRequest)
    }

    override fun patchMemo(scheduleID: Int, patchMemo: PatchMemo): Single<BaseResponse> {
        return api.patchMemo(scheduleID, patchMemo)
    }

    override fun getDetailMemo(scheduleID: Int): Observable<DetailMemoDto> {
        return api.getDetailMemo(scheduleID)
    }

    override fun getUserCategoryInquiry(): Observable<CategoryDto> {
        return api.getUserCategoryInquiry()
    }

    override fun getCategoryInquiry(
        scheduleCategoryID: Int,
        offset: Int,
        limit: Int
    ): Observable<CategoryInquiryResponse> {
        return api.getCategoryInquiry(scheduleCategoryID, offset, limit)
    }
}