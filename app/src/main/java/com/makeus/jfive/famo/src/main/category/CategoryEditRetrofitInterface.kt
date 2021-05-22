package com.makeus.jfive.famo.src.main.category

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.category.models.CategoryInsertRequest
import com.makeus.jfive.famo.src.main.category.models.CategoryInsertResponse
import com.makeus.jfive.famo.src.main.category.models.CategoryUpdateRequest
import retrofit2.Call
import retrofit2.http.*

interface CategoryEditRetrofitInterface {

    //카테고리 생성
    @POST("categories")
    fun getCategoryInsert(@Body categoryInsertRequest: CategoryInsertRequest):
            Call<CategoryInsertResponse>

    //카테고리 삭제
    @DELETE("categories/{categoryID}")
    fun getCategoryDelete(@Path("categoryID") categoryID : String): Call<BaseResponse>

    //카테고리 수정
    @PATCH("categories/{categoryID}")
    fun getCategoryUpdate(@Path("categoryID") categoryID : String,
        @Body categoryUpdateRequest: CategoryUpdateRequest): Call<BaseResponse>

}