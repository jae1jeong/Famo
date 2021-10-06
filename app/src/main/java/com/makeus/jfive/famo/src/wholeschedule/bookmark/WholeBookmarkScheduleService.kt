package com.makeus.jfive.famo.src.wholeschedule.bookmark

import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.src.main.schedulefind.models.ScheduleBookmarkResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WholeBookmarkScheduleService(val view : WholeBookmarkScheduleView) {

    //전체일정
    fun tryGetScheduleBookmark(offset : Int, limit : Int){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(
            WholeBookmarkScheduleRetrofitInterface::class.java)

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