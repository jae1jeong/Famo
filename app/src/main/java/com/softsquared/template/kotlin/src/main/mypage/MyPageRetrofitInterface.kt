package com.softsquared.template.kotlin.src.main.mypage

import com.softsquared.template.kotlin.src.main.mypage.models.MyPageCommentsResponse
import retrofit2.Call
import retrofit2.http.GET

interface MyPageRetrofitInterface {

    //상단멘트
    @GET("profiles/comments")
    fun getMyPageComments() : Call<MyPageCommentsResponse>

}