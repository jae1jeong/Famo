package com.softsquared.template.kotlin.src.main.mypage.edit

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageEditCommentsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageEditService(val editView : MyPageEditView) {

    fun tryGetMyPage(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageEditRetrofitInterface::class.java)

        homeRetrofitInterface.getMyPage().enqueue(object :
            Callback<MyPageEditCommentsResponse> {
            override fun onResponse(call: Call<MyPageEditCommentsResponse>, editCommentsResponse: Response<MyPageEditCommentsResponse>) {
                Log.d("값 확인", "tryGetMyPage body:  ${editCommentsResponse.body()}")
                Log.d("값 확인", "tryGetMyPage code:  ${editCommentsResponse.code()}")
                editView.onGetMyPageEditSuccess(editCommentsResponse.body() as MyPageEditCommentsResponse)
            }

            override fun onFailure(call: Call<MyPageEditCommentsResponse>, t: Throwable) {
                editView.onGetMyPageEditFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }

}