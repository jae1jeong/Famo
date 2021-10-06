package com.makeus.jfive.famo.src.domain.repository

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.domain.model.main.*
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.CategoryInquiryResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.UserCategoryInquiryResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface MainRepository {

    // 남은 일정수 조회(전체/오늘)
    fun getRestScheduleCount(date:String): Observable<RemainScheduleCountDto>

    fun getDoneScheduleCount():Observable<DoneScheduleCountDto>

    fun postAddMemo(memoRequest: PostTodayRequestAddMemo): Single<BaseResponse>

    fun patchMemo(scheduleID :Int,patchMemo: PatchMemo): Single<BaseResponse>

    fun getDetailMemo(scheduleID: Int): Observable<DetailMemoDto>

    //유져별 카테고리 조회
    fun getUserCategoryInquiry() : Observable<CategoryDto>

    //일정별 카테고리조회
    fun getCategoryInquiry(scheduleCategoryID : Int,
                           offset : Int,
                           limit : Int) : Observable<CategoryInquiryResponse>

}