package com.softsquared.template.kotlin.src.main.mypage

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.main.mypage.models.DoneScheduleCountResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.main.mypage.models.RestScheduleCountResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageService(val pageView : MyPageView) {

    fun tryGetMyPage(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageRetrofitInterface::class.java)

        homeRetrofitInterface.getMyPage().enqueue(object :
            Callback<MyPageResponse> {
            override fun onResponse(call: Call<MyPageResponse>, response: Response<MyPageResponse>) {
                Log.d("값 확인", "tryGetMyPage body:  ${response.body()}")
                Log.d("값 확인", "tryGetMyPage code:  ${response.code()}")
                pageView.onGetMyPageSuccess(response.body() as MyPageResponse)
            }

            override fun onFailure(call: Call<MyPageResponse>, t: Throwable) {
                pageView.onGetMyPageFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }

//    fun tryGetMyPageComments(){
//        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageRetrofitInterface::class.java)
//
//        homeRetrofitInterface.getMyPageComments().enqueue(object :
//            Callback<MyPageCommentsResponse> {
//            override fun onResponse(call: Call<MyPageCommentsResponse>, response: Response<MyPageCommentsResponse>) {
//                Log.d("값 확인", "tryGetMyPage body:  ${response.body()}")
//                Log.d("값 확인", "tryGetMyPage code:  ${response.code()}")
//                editView.onGetMyPageCommentsSuccess(response.body() as MyPageCommentsResponse)
//            }
//
//            override fun onFailure(call: Call<MyPageCommentsResponse>, t: Throwable) {
//                editView.onGetMyPageCommentsFail(t.message ?: "통신 오류")
////                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
//            }
//        })
//    }

    fun tryGetRestScheduleCount(date:String){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageRetrofitInterface::class.java)
        homeRetrofitInterface.getRestScheduleCount(date).enqueue(object:Callback<RestScheduleCountResponse>{
            override fun onResponse(call: Call<RestScheduleCountResponse>, response: Response<RestScheduleCountResponse>) {
                pageView.onGetRestScheduleCountSuccess(response.body() as RestScheduleCountResponse)
            }

            override fun onFailure(call: Call<RestScheduleCountResponse>, t: Throwable) {
                pageView.onGetRestScheduleCountFailure(t.message ?: "남은 일정수 조회 관련 통신 오류")
            }

        })
    }

    fun tryGetDoneScheduleCount(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageRetrofitInterface::class.java)
        homeRetrofitInterface.getDoneScheduleCount().enqueue(object:Callback<DoneScheduleCountResponse>{
            override fun onResponse(call: Call<DoneScheduleCountResponse>, response: Response<DoneScheduleCountResponse>) {
                pageView.onGetDoneScheduleCountSuccess(response.body() as DoneScheduleCountResponse)
            }

            override fun onFailure(call: Call<DoneScheduleCountResponse>, t: Throwable) {
                pageView.onGetDoneScheduleCountFailure(t.message ?: "해낸 일정수 조회 관련 통신 오류")
            }

        })

    }
}