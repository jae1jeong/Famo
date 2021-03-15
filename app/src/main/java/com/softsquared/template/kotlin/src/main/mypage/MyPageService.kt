package com.softsquared.template.kotlin.src.main.mypage

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageCommentsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageService(val editView : MyPageView) {

    fun tryGetMyPageComments(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageRetrofitInterface::class.java)

        homeRetrofitInterface.getMyPageComments().enqueue(object :
            Callback<MyPageCommentsResponse> {
            override fun onResponse(call: Call<MyPageCommentsResponse>, response: Response<MyPageCommentsResponse>) {
                Log.d("값 확인", "tryGetMyPage body:  ${response.body()}")
                Log.d("값 확인", "tryGetMyPage code:  ${response.code()}")
                editView.onGetMyPageCommentsSuccess(response.body() as MyPageCommentsResponse)
            }

            override fun onFailure(call: Call<MyPageCommentsResponse>, t: Throwable) {
                editView.onGetMyPageCommentsFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }
}