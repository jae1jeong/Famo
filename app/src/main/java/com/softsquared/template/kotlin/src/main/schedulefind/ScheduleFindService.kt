package com.softsquared.template.kotlin.src.main.schedulefind

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleFindService(val view : ScheduleFindView) {

    fun tryGetWholeScheduleInquiry(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(ScheduleFindRetrofitInterface::class.java)

        homeRetrofitInterface.getWholeScheduleInquiry().enqueue(object :
            Callback<WholeScheduleInquiryResponse> {
            override fun onResponse(call: Call<WholeScheduleInquiryResponse>, responseUser: Response<WholeScheduleInquiryResponse>) {
                Log.d("값 확인", "tryGetUserCategoryInquiry body:  ${responseUser.body()}")
                Log.d("값 확인", "tryGetUserCategoryInquiry code:  ${responseUser.code()}")
                view.onGetWholeScheduleInquirySuccess(responseUser.body() as WholeScheduleInquiryResponse)
            }

            override fun onFailure(call: Call<WholeScheduleInquiryResponse>, t: Throwable) {
                view.onGetWholeScheduleInquiryFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }


}