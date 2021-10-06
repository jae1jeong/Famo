package com.makeus.jfive.famo.src.main.today

import android.util.Log
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse
import com.makeus.jfive.famo.src.main.today.models.ChangePositionItemRequest
import com.makeus.jfive.famo.src.main.today.models.CheckItemRequest
import com.makeus.jfive.famo.src.main.today.models.ScheduleItemsResponse
import com.makeus.jfive.famo.src.main.today.models.TopCommentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodayService(val view:TodayView){

    fun onGetScheduleItems(){
        val todayRetrofitInterface = ApplicationClass.sRetrofit.create(TodayRetrofitInterface::class.java)
        todayRetrofitInterface.getTodayMemos().enqueue(object: Callback<ScheduleItemsResponse>{
            override fun onResponse(call: Call<ScheduleItemsResponse>, response: Response<ScheduleItemsResponse>) {
                view.onGetScheduleItemsSuccess(response.body() as ScheduleItemsResponse)
            }

            override fun onFailure(call: Call<ScheduleItemsResponse>, t: Throwable) {
                view.onGetScheduleItemsFailure(t.message ?: "스케쥴 일정 조회 관련 통신 오류")
            }

        })
    }

    fun onPutDeleteMemo(scheduleID:Int){
        val todayRetrofitInterface = ApplicationClass.sRetrofit.create(TodayRetrofitInterface::class.java)
        todayRetrofitInterface.deleteMemo(scheduleID).enqueue(object:Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onDeleteMemoSuccess(response.body() as BaseResponse,scheduleID)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onDeleteMemoFailure(t.message ?: "메모 삭제 관련 통신 오류")
            }

        })
    }

    fun onPostCheckItem(checkItemRequest: CheckItemRequest){
        val todayRetrofitInterface = ApplicationClass.sRetrofit.create(TodayRetrofitInterface::class.java)
        todayRetrofitInterface.postItemCheck(checkItemRequest).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                Log.d("TAG", "onResponse: $response")
                view.onPostItemCheckSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostItemCheckFailure(t.message ?: "메모 일정 체크 관련 통신 오류")
            }

        })
    }

    fun onGetTopComment(){
        val todayRetrofitInterface = ApplicationClass.sRetrofit.create(TodayRetrofitInterface::class.java)
        todayRetrofitInterface.getTopComment().enqueue(object:Callback<TopCommentResponse>{
            override fun onResponse(call: Call<TopCommentResponse>, response: Response<TopCommentResponse>) {
                view.onGetUserTopCommentSuccess(response.body() as TopCommentResponse)
            }

            override fun onFailure(call: Call<TopCommentResponse>, t: Throwable) {
                view.onGetUserTopCommentFailure(t.message ?: "상단 멘트 관련 통신 오류")
            }

        })
    }

    fun onPostChangeItemPosition(changePositionItemRequest: ChangePositionItemRequest){
        val todayRetrofitInterface = ApplicationClass.sRetrofit.create(TodayRetrofitInterface::class.java)
        todayRetrofitInterface.postChangeItemPosition(changePositionItemRequest).enqueue(object:Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.onPostSchedulePositionSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.onPostSchedulePositionFailure(t.message ?: "일정 순서 변경 관련 통신 오류")
            }

        })
    }

    fun tryGetDetailMemo(scheduleID: Int){
        val todayRetrofitInterface = ApplicationClass.sRetrofit.create(TodayRetrofitInterface::class.java)
        todayRetrofitInterface.getDetailMemo(scheduleID).enqueue(object:Callback<DetailMemoResponse>{
            override fun onResponse(call: Call<DetailMemoResponse>, response: Response<DetailMemoResponse>) {
                view.onGetDetailMemoSuccess(response.body() as DetailMemoResponse)
            }

            override fun onFailure(call: Call<DetailMemoResponse>, t: Throwable) {
                view.onGetDetailMemoFailure(t.message ?: "일정 상세 조회 관련 통신 오류")
            }

        })

    }
}