package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryFilterResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoryFilterRetrofitInterface {

    //카테고리 필터 조회
    @GET("category-schedules")
    fun getCategoryFilterInquiry(@Query("scheduleCategoryID") scheduleCategoryID : Int,
                                 @Query("sort") sort : String,
                                 @Query("offset") offset : Int,
                                 @Query("limit") limit : Int) : Call<CategoryFilterResponse>

}