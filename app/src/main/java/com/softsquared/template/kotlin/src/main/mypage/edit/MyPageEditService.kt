package com.softsquared.template.kotlin.src.main.mypage.edit

import android.util.Log
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageCommentsResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.main.mypage.models.PutMyPageUpdateRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageEditService(val editView : MyPageEditView) {

    fun tryGetMyPageComments(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageEditRetrofitInterface::class.java)
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

    fun tryGetMyPage(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageEditRetrofitInterface::class.java)
        homeRetrofitInterface.getMyPage().enqueue(object :
            Callback<MyPageResponse> {
            override fun onResponse(call: Call<MyPageResponse>, response: Response<MyPageResponse>) {
                Log.d("값 확인", "tryGetMyPage body:  ${response.body()}")
                Log.d("값 확인", "tryGetMyPage code:  ${response.code()}")
                editView.onGetMyPageSuccess(response.body() as MyPageResponse)
            }

            override fun onFailure(call: Call<MyPageResponse>, t: Throwable) {
                editView.onGetMyPageFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }

    fun tryPutMyPageUpdate(myPageUpdateRequest: PutMyPageUpdateRequest){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageEditRetrofitInterface::class.java)
        homeRetrofitInterface.putMyPageUpdate(myPageUpdateRequest).enqueue(object :
            Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                Log.d("값 확인", "tryGetMyPage body:  ${response.body()}")
                Log.d("값 확인", "tryGetMyPage code:  ${response.code()}")
                editView.onPutMyPageUpdateSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                editView.onPutMyPageUpdateFail(t.message ?: "통신 오류")
//                Log.d("인증실패했니", "onGetProfileInqueryFail:  ")
            }
        })
    }

}