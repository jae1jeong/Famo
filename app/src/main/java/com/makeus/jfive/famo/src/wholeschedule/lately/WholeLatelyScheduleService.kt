package com.makeus.jfive.famo.src.wholeschedule.lately

import android.util.Log
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.src.wholeschedule.models.LatelyScheduleInquiryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WholeLatelyScheduleService(val viewLately : WholeLatelyScheduleView) {

    fun tryGetLatelyScheduleInquiry(offset : Int, limit : Int){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(WholeLatelyScheduleRetrofitInterface::class.java)

        homeRetrofitInterface.getLatelyWholeScheduleInquiry(offset, limit).enqueue(object :
            Callback<LatelyScheduleInquiryResponse> {
            override fun onResponse(
                call: Call<LatelyScheduleInquiryResponse>,
                response: Response<LatelyScheduleInquiryResponse>
            ) {
                Log.d("값 확인", "tryGetLatelyScheduleInquiry body:  ${response.body()}")
                Log.d("값 확인", "tryGetLatelyScheduleInquiry code:  ${response.code()}")
                viewLately.onGetLatelyScheduleInquirySuccess(response.body() as LatelyScheduleInquiryResponse)
            }

            override fun onFailure(call: Call<LatelyScheduleInquiryResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: 전체최근일정조회오류확인 ${t.message}")
                viewLately.onGetLatelyScheduleInquiryFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }
}