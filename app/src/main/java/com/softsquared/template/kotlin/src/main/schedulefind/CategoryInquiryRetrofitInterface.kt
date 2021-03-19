package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoryInquiryRetrofitInterface {

    //유져별 카테고리 조회
    @GET("categories")
    fun getUserCategoryInquiry() : Call<UserCategoryInquiryResponse>

    //일정별 카테고리조회
    @GET("category-schedules")
    fun getCategoryInquiry(@Query("scheduleCategoryID") scheduleCategoryID : Int,
                           @Query("offset") offset : Int,
                           @Query("limit") limit : Int) : Call<CategoryInquiryResponse>
}