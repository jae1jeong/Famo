package com.softsquared.template.kotlin.src.main.schedulefind

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.mypage.MyPageRetrofitInterface
import com.softsquared.template.kotlin.src.main.mypage.models.RestScheduleCountResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.BookmarkRequest
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleCountResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleInquiryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleFindService(val view : ScheduleFindView) {

    //전체일정
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

    //즐겨찾기
    fun tryPostBookmark(bookmarkRequest: BookmarkRequest){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(ScheduleFindRetrofitInterface::class.java)

        homeRetrofitInterface.postBookmark(bookmarkRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, responser: Response<BaseResponse>) {
                Log.d("값 확인", "tryPostBookmark body:  ${responser.body()}")
                Log.d("값 확인", "tryPostBookmark code:  ${responser.code()}")
                view.onPostBookmarkSuccess(responser.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostBookmarkFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }


    //전체일정수
    fun tryGetWholeScheduleCount(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(ScheduleFindRetrofitInterface::class.java)
        homeRetrofitInterface.getWholeScheduleCount().enqueue(object:Callback<WholeScheduleCountResponse>{
            override fun onResponse(call: Call<WholeScheduleCountResponse>, response: Response<WholeScheduleCountResponse>) {
                view.onGetWholeScheduleCountSuccess(response.body() as WholeScheduleCountResponse)
            }

            override fun onFailure(call: Call<WholeScheduleCountResponse>, t: Throwable) {
                view.onGetWholeScheduleCountFailure(t.message ?: "남은 일정수 조회 관련 통신 오류")
            }

        })
    }

}