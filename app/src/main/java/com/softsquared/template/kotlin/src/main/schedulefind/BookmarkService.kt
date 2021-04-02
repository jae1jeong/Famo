package com.softsquared.template.kotlin.src.main.schedulefind

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarkService(val view : BookmarkView) {

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

}