package com.softsquared.template.kotlin.src.mypageedit

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.mypageedit.models.MyPageCommentsResponse
import com.softsquared.template.kotlin.src.mypageedit.models.PutMyPageUpdateRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface MyPageEditRetrofitInterface {

    //프로필 조회
    @GET("profiles")
    fun getMyPage() : Call<MyPageResponse>

    //상단멘트
    @GET("profiles/comments")
    fun getMyPageComments() : Call<MyPageCommentsResponse>

    //상단멘트
    @PUT("profiles")
    fun putMyPageUpdate(@Body putMyPageUpdateRequest : PutMyPageUpdateRequest)
        : Call<BaseResponse>

}