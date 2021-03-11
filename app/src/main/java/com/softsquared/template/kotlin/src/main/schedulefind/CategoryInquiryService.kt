package com.softsquared.template.kotlin.src.main.schedulefind

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryInquiryService(val view : CategoryInquiryView) {

    fun tryGetCategoryInquiry(token: String){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(CategoryInquiryRetrofitInterface::class.java)

        homeRetrofitInterface.getCategoryInquiry(token).enqueue(object :
            Callback<CategoryInquiryResponse> {
            override fun onResponse(call: Call<CategoryInquiryResponse>, response: Response<CategoryInquiryResponse>) {
                Log.d("값 확인", "tryGetCategoryInquiry body:  ${response.body()}")
                Log.d("값 확인", "tryGetCategoryInquiry code:  ${response.code()}")
                view.onGetCategoryInquirySuccess(response.body() as CategoryInquiryResponse)
            }

            override fun onFailure(call: Call<CategoryInquiryResponse>, t: Throwable) {
                view.onGetCategoryInquiryFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }
}