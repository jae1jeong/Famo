package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import retrofit2.Call
import retrofit2.http.GET

interface CategoryInquiryRetrofitInterface {

    //유져별 카테고리 조회
    @GET("categories")
    fun getUserCategoryInquiry() : Call<UserCategoryInquiryResponse>

    //일정별 카테고리조회
    @GET("schedules?scheduleCategoryID=")
    fun getCategoryInquiry() : Call<CategoryInquiryResponse>
}