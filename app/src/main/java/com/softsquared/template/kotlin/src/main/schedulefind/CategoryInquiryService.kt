package com.softsquared.template.kotlin.src.main.schedulefind

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryInquiryService(val view : CategoryInquiryView) {

    fun tryGetUserCategoryInquiry(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(CategoryInquiryRetrofitInterface::class.java)

        homeRetrofitInterface.getUserCategoryInquiry().enqueue(object :
            Callback<UserCategoryInquiryResponse> {
            override fun onResponse(call: Call<UserCategoryInquiryResponse>, responseUser: Response<UserCategoryInquiryResponse>) {
                Log.d("값 확인", "tryGetCategoryInquiry body:  ${responseUser.body()}")
                Log.d("값 확인", "tryGetCategoryInquiry code:  ${responseUser.code()}")
                view.onGetUserCategoryInquirySuccess(responseUser.body() as UserCategoryInquiryResponse)
            }

            override fun onFailure(call: Call<UserCategoryInquiryResponse>, t: Throwable) {
                view.onGetUserCategoryInquiryFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }

    fun tryGetCategoryInquiry(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(CategoryInquiryRetrofitInterface::class.java)

        homeRetrofitInterface.getCategoryInquiry().enqueue(object :
            Callback<CategoryInquiryResponse> {
            override fun onResponse(call: Call<CategoryInquiryResponse>, responseUser: Response<CategoryInquiryResponse>) {
                Log.d("값 확인", "tryGetCategoryInquiry body:  ${responseUser.body()}")
                Log.d("값 확인", "tryGetCategoryInquiry code:  ${responseUser.code()}")
                view.onGetCategoryInquirySuccess(responseUser.body() as CategoryInquiryResponse)
            }

            override fun onFailure(call: Call<CategoryInquiryResponse>, t: Throwable) {
                view.onGetCategoryInquiryFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }
}