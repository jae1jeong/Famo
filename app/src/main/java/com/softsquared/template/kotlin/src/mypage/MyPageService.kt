package com.softsquared.template.kotlin.src.mypage

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.mypage.models.MonthsAchievementsResponse
import com.softsquared.template.kotlin.src.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.mypage.models.RestScheduleCountResponse
import com.softsquared.template.kotlin.src.mypage.models.TotalScheduleCountResponse
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

    //전체 일정/해낸 수
    fun tryGetTotalScheduleCount(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageRetrofitInterface::class.java)
        homeRetrofitInterface.getTotalScheduleCount().enqueue(object:Callback<TotalScheduleCountResponse>{
            override fun onResponse(call: Call<TotalScheduleCountResponse>, response: Response<TotalScheduleCountResponse>) {
                pageView.onGetTotalScheduleCountSuccess(response.body() as TotalScheduleCountResponse)
            }

            override fun onFailure(call: Call<TotalScheduleCountResponse>, t: Throwable) {
                pageView.onGetTotalScheduleCountFailure(t.message ?: "해낸 일정수 조회 관련 통신 오류")
            }

        })
    }

    //월별 달성률
    fun tryGetMonthsAchievement(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageRetrofitInterface::class.java)
        homeRetrofitInterface.getMonthsAchievements().enqueue(object :
            Callback<MonthsAchievementsResponse> {
            override fun onResponse(
                call: Call<MonthsAchievementsResponse>, response: Response<MonthsAchievementsResponse>) {
                Log.d("TAG", "tryGetMonthsAchievement: ${response.body()}")
                pageView.onGetMonthsAchievmentsSuccess(response.body() as MonthsAchievementsResponse)
            }

            override fun onFailure(call: Call<MonthsAchievementsResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: 월별달성률 실패 ${t.message}")
                pageView.onGetMonthsAchievmentsFailure(t.message ?: "해낸 일정수 조회 관련 통신 오류")
            }

        })
    }
}