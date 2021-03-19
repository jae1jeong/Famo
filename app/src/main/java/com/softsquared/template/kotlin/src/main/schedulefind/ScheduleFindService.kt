package com.softsquared.template.kotlin.src.main.schedulefind

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleFindService(val view : ScheduleFindView) {

    fun tryGetWholeScheduleInquiry(offset : Int, limit : Int){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(ScheduleFindRetrofitInterface::class.java)

        homeRetrofitInterface.getWholeScheduleInquiry(offset, limit).enqueue(object :
            Callback<WholeScheduleInquiryResponse> {
            override fun onResponse(call: Call<WholeScheduleInquiryResponse>, response: Response<WholeScheduleInquiryResponse>) {
//                Log.d("값 확인", "tryGetWholeScheduleInquiry body:  ${response.body()}")
//                Log.d("값 확인", "tryGetWholeScheduleInquiry code:  ${response.code()}")
                view.onGetWholeScheduleInquirySuccess(response.body() as WholeScheduleInquiryResponse)
            }

            override fun onFailure(call: Call<WholeScheduleInquiryResponse>, t: Throwable) {
                view.onGetWholeScheduleInquiryFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }

    fun tryPostBookmark(scheduleID : Int){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(ScheduleFindRetrofitInterface::class.java)

        homeRetrofitInterface.postBoorkmark(scheduleID).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, responseUser: Response<BaseResponse>) {
                Log.d("값 확인", "tryPostBookmark body:  ${responseUser.body()}")
                Log.d("값 확인", "tryPostBookmark code:  ${responseUser.code()}")
                view.onPostBookmarkSuccess(responseUser.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostBookmarkFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }


}