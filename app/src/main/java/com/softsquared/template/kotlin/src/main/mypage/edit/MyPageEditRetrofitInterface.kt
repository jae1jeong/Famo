package com.softsquared.template.kotlin.src.main.mypage.edit

import com.softsquared.template.kotlin.src.main.mypage.models.MyPageEditResponse
import retrofit2.Call
import retrofit2.http.GET

interface MyPageEditRetrofitInterface {

    //프로필 조회
    @GET("profiles")
    fun getMyPage() : Call<MyPageEditResponse>

}