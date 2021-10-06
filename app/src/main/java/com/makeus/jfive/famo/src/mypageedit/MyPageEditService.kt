package com.makeus.jfive.famo.src.mypageedit

import android.util.Log
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.mypageedit.models.MyPageCommentsResponse
import com.makeus.jfive.famo.src.mypage.models.MyPageResponse
import com.makeus.jfive.famo.src.mypageedit.models.PutMyPageUpdateRequest
import com.makeus.jfive.famo.src.mypageedit.models.SetProfileImageResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageEditService(val editView : MyPageEditView) {

    //상단멘트
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
            }
        })
    }

    //내정보 조회
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
            }
        })
    }

    //수정
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
            }
        })
    }


    // 프로필 사진 설정
    fun tryPostMyProfileImage(file:MultipartBody.Part){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageEditRetrofitInterface::class.java)
        homeRetrofitInterface.postMyProfileImage(file).enqueue(object:Callback<SetProfileImageResponse>{
            override fun onResponse(call: Call<SetProfileImageResponse>, response: Response<SetProfileImageResponse>) {
                editView.onPostProfileImageSuccess(response.body() as SetProfileImageResponse)
            }

            override fun onFailure(call: Call<SetProfileImageResponse>, t: Throwable) {
                editView.onPostProfileImageFailure(t.message ?: "프로필 사진 설정 관련 통신 오류")
            }

        })
    }

    // 프로필 삭제
    fun tryPatchMyProfileImage(){
        val homeRetrofitInterface = ApplicationClass.sRetrofit.create(MyPageEditRetrofitInterface::class.java)
        homeRetrofitInterface.patchMyProfileImage().enqueue(object:Callback<BaseResponse>{
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                editView.onPatchProfileSuccess(response.body() as BaseResponse)
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                editView.onPatchProfileFailure(t.message ?: "프로필 삭제 관련 통신 오류")
            }

        })
    }

}