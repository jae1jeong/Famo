package com.softsquared.template.kotlin.src.main.category

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertRequest
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryEditService(val view: CategoryEditView) {

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

    fun tryPatchCategoryEditUpdate(categoryID : String){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(CategoryEditRetrofitInterface::class.java)

        homeRetrofitInterface.getCategoryUpdate(categoryID).enqueue(object :
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