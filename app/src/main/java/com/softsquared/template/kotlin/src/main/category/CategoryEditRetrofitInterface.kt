package com.softsquared.template.kotlin.src.main.category

import com.bumptech.glide.request.BaseRequestOptions
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertRequest
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertResponse
import retrofit2.Call
import retrofit2.http.*

interface CategoryEditRetrofitInterface {

    //카테고리 생성
    @POST("categories")
    fun getCategoryInsert(@Header("x-access-token") token: String,
        @Body categoryInsertRequest: CategoryInsertRequest): Call<CategoryInsertResponse>

    //카테고리 삭제
    @DELETE("categories/{categoryID}")
    fun getCategoryDelete(@Header("x-access-token") token: String,
                          @Path("categoryID") categoryID : Int): Call<BaseResponse>

    //카테고리 수정
    @PATCH("categories/{categoryID}")
    fun getCategoryUpdate(@Header("x-access-token") token: String,
                          @Path("categoryID") categoryID : Int): Call<BaseResponse>

}