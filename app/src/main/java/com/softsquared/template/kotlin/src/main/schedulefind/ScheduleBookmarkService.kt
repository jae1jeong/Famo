package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkResponse
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
                view.onGetScheduleBookmarkSuccess(response.body() as ScheduleBookmarkResponse)
            }

            override fun onFailure(call: Call<ScheduleBookmarkResponse>, t: Throwable) {
                view.onGetScheduleBookmarkFail(t.message ?: "통신 오류")
            }
        })
    }

}