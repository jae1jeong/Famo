package com.makeus.jfive.famo.src.domain.repository

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.domain.model.today.PostMemoCheck
import com.makeus.jfive.famo.src.domain.model.today.TodayMemoList
import com.makeus.jfive.famo.src.domain.model.today.TopComment
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse
import com.makeus.jfive.famo.src.main.today.models.TopCommentResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

interface TodayRepository {

    fun getTodayMemos(): Single<TodayMemoList>

    fun deleteMemo(@Path("scheduleID") scheduleID:Int): Single<BaseResponse>

    fun postItemCheck(postMemoCheck: PostMemoCheck): Observable<BaseResponse>

    fun getTopComment(): Single<TopComment>

    fun postChangeItemPosition(scheduleID: Int): Single<BaseResponse>

    fun getDetailMemo(scheduleID: Int): Single<DetailMemoResponse>
}