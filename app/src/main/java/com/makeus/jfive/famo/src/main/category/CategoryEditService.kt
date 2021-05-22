package com.makeus.jfive.famo.src.main.category

import android.util.Log
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.category.models.CategoryInsertRequest
import com.makeus.jfive.famo.src.main.category.models.CategoryInsertResponse
import com.makeus.jfive.famo.src.main.category.models.CategoryUpdateRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryEditService(val view: CategoryEditView) {

    //카테고리 생성
    fun tryPostCategoryEditInsert(categoryInsertRequest : CategoryInsertRequest){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(CategoryEditRetrofitInterface::class.java)

        homeRetrofitInterface.getCategoryInsert(categoryInsertRequest).enqueue(object :
            Callback<CategoryInsertResponse> {
            override fun onResponse(call: Call<CategoryInsertResponse>, response: Response<CategoryInsertResponse>) {
                Log.d("값 확인", "tryPostCategoryEditInsert body:  ${response.body()}")
                Log.d("값 확인", "tryPostCategoryEditInsert code:  ${response.code()}")
                view.onPostCategoryInsertSuccess(response.body() as CategoryInsertResponse)
            }

            override fun onFailure(call: Call<CategoryInsertResponse>, t: Throwable) {
                view.onPostCategoryInsertFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }

    //카테고리 삭제
    fun tryDeleteCategoryEditDelete(categoryID : String){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(CategoryEditRetrofitInterface::class.java)

        homeRetrofitInterface.getCategoryDelete(categoryID).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                Log.d("값 확인", "tryPostCategoryEditDelete body:  ${response.body()}")
                Log.d("값 확인", "tryPostCategoryEditDelete code:  ${response.code()}")
                view.onDeleteCategoryDeleteSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onDeleteCategoryDeleteFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }

    //카테고리 수정
    fun tryPatchCategoryEditUpdate(categoryID : String, categoryUpdateRequest: CategoryUpdateRequest){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(CategoryEditRetrofitInterface::class.java)

        homeRetrofitInterface.getCategoryUpdate(categoryID,categoryUpdateRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                Log.d("값 확인", "tryPostCategoryEditDelete body:  ${response.body()}")
                Log.d("값 확인", "tryPostCategoryEditDelete code:  ${response.code()}")
                view.onPatchCategoryUpdateSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPatchCategoryUpdateFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }



}