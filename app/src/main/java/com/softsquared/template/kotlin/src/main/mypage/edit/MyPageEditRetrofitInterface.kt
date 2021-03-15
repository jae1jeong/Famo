package com.softsquared.template.kotlin.src.main.mypage.edit

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageCommentsResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageEditRequest
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface MyPageEditRetrofitInterface {

    //프로필 조회
    @GET("profiles")
    fun getMyPage() : Call<MyPageResponse>

//    상단멘트
    @GET("profiles/comments")
    fun getMyPageComments() : Call<MyPageCommentsResponse>

}