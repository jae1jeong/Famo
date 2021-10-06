package com.makeus.jfive.famo.src.main.schedulefind

import android.util.Log
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.schedulefind.models.*
import com.makeus.jfive.famo.src.wholeschedule.models.LatelyScheduleInquiryResponse
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
                Log.d("값 확인", "tryGetWholeScheduleInquiry body:  ${response.body()}")
                Log.d("값 확인", "tryGetWholeScheduleInquiry code:  ${response.code()}")
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

    //최근일정
    fun tryGetLatelyScheduleFindInquiry(offset : Int, limit : Int){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(ScheduleFindRetrofitInterface::class.java)

        homeRetrofitInterface.getLatelyScheduleInquiry(offset, limit).enqueue(object :
            Callback<LatelyScheduleInquiryResponse> {
            override fun onResponse(call: Call<LatelyScheduleInquiryResponse>, response: Response<LatelyScheduleInquiryResponse>) {
                Log.d("값 확인", "tryGetLatelyScheduleInquiry body:  ${response.body()}")
                Log.d("값 확인", "tryGetLatelyScheduleInquiry code:  ${response.code()}")
                view.onGetLatelyScheduleFindInquirySuccess(response.body() as LatelyScheduleInquiryResponse)
            }

            override fun onFailure(call: Call<LatelyScheduleInquiryResponse>, t: Throwable) {
                view.onGetLatelySchedulefindInquiryFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }

    //남은일정
    fun tryGetRestScheduleCount(date:String){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(ScheduleFindRetrofitInterface::class.java)
        homeRetrofitInterface.getRestScheduleCount(date).enqueue(object:Callback<TodayRestScheduleResponse>{
            override fun onResponse(call: Call<TodayRestScheduleResponse>, response: Response<TodayRestScheduleResponse>) {
                view.onGetTodayRestScheduleSuccess(response.body() as TodayRestScheduleResponse)
            }

            override fun onFailure(call: Call<TodayRestScheduleResponse>, t: Throwable) {
                view.onGetTodayRestScheduleFail(t.message ?: "남은 일정수 조회 관련 통신 오류")
            }

        })
    }

    fun tryGetScheduleSearch(searchWord: String){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(ScheduleFindRetrofitInterface::class.java)
        homeRetrofitInterface.getScheduleSearch(searchWord).enqueue(object:Callback<ScheduleSearchResponse>{
            override fun onResponse(call: Call<ScheduleSearchResponse>, response: Response<ScheduleSearchResponse>) {
                Log.d("값 확인", "tryGetScheduleSearch body:  ${response.body()}")
                Log.d("값 확인", "tryGetScheduleSearch body:  ${response.body()}")
                view.onGetScheduleSearchSuccess(response.body() as ScheduleSearchResponse)
            }

            override fun onFailure(call: Call<ScheduleSearchResponse>, t: Throwable) {
                view.onGetScheduleSearchFail(t.message ?: "남은 일정수 조회 관련 통신 오류")
            }

        })
    }

}