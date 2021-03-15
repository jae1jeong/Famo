package com.softsquared.template.kotlin.src.main.mypage.edit

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageEditResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageEditService(val editView : MyPageEditView) {

    fun tryGetMyPage(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageEditRetrofitInterface::class.java)

        homeRetrofitInterface.getMyPage().enqueue(object :
            Callback<MyPageEditResponse> {
            override fun onResponse(call: Call<MyPageEditResponse>, editResponse: Response<MyPageEditResponse>) {
                Log.d("값 확인", "tryGetMyPage body:  ${editResponse.body()}")
                Log.d("값 확인", "tryGetMyPage code:  ${editResponse.code()}")
                editView.onGetMyPageEditSuccess(editResponse.body() as MyPageEditResponse)
            }

            override fun onFailure(call: Call<MyPageEditResponse>, t: Throwable) {
                editView.onGetMyPageEditFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }

}