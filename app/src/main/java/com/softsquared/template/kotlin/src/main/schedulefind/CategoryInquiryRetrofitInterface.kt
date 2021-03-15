package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import retrofit2.Call
import retrofit2.http.GET

interface CategoryInquiryRetrofitInterface {

    //카테고리 조회
    @GET("categories")
    fun getCategoryInquiry() : Call<UserCategoryInquiryResponse>
}