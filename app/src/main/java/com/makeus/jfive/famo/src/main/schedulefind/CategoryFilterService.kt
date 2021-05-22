package com.makeus.jfive.famo.src.main.schedulefind

import android.util.Log
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.src.main.schedulefind.models.CategoryFilterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFilterService(val view : CategoryFilterView) {

    fun tryGetFilterCategoryInquiry(scheduleCategoryID : Int, sort : String, offset : Int, limit : Int){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(CategoryFilterRetrofitInterface::class.java)

        homeRetrofitInterface.getCategoryFilterInquiry(scheduleCategoryID,sort,offset,limit).enqueue(object :
            Callback<CategoryFilterResponse> {
            override fun onResponse(call: Call<CategoryFilterResponse>, responseUser: Response<CategoryFilterResponse>) {
                Log.d("값 확인", "tryGetUserCategoryInquiry body:  ${responseUser.body()}")
                Log.d("값 확인", "tryGetUserCategoryInquiry code:  ${responseUser.code()}")
                view.onGetCategoryFilterInquirySuccess(responseUser.body() as CategoryFilterResponse)
            }

            override fun onFailure(call: Call<CategoryFilterResponse>, t: Throwable) {
                view.onGetCategoryFilterInquiryFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }

}