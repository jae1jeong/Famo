package com.softsquared.template.kotlin.src.main.schedulefind

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.mypage.MyPageRetrofitInterface
import com.softsquared.template.kotlin.src.main.mypage.models.RestScheduleCountResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.BookmarkRequest
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleCountResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryResponse
import com.softsquared.template.kotlin.src.wholeschedule.WholeScheduleRetrofitInterface
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleBookmarkService(val view : ScheduleBookmarkView) {

    //전체일정
    fun tryGetScheduleBookmark(offset : Int, limit : Int){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(ScheduleFindBookmarkRetrofitInterface::class.java)

        homeRetrofitInterface.getScheduleBookmark(offset, limit).enqueue(object :
            Callback<ScheduleBookmarkResponse> {
            override fun onResponse(call: Call<ScheduleBookmarkResponse>, response: Response<ScheduleBookmarkResponse>) {
//                Log.d("값 확인", "tryGetWholeScheduleInquiry body:  ${response.body()}")
//                Log.d("값 확인", "tryGetWholeScheduleInquiry code:  ${response.code()}")
                view.onGetScheduleBookmarkSuccess(response.body() as ScheduleBookmarkResponse)
            }

            override fun onFailure(call: Call<ScheduleBookmarkResponse>, t: Throwable) {
                view.onGetScheduleBookmarkFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }

}