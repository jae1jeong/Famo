package com.makeus.jfive.famo.src.mypageedit

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.mypageedit.models.MyPageCommentsResponse
import com.makeus.jfive.famo.src.mypage.models.MyPageResponse
import com.makeus.jfive.famo.src.mypageedit.models.SetProfileImageResponse

interface MyPageEditView {

    //상단멘트
    fun onGetMyPageCommentsSuccess(response: MyPageCommentsResponse)
    fun onGetMyPageCommentsFail(message: String)

    //마이페이지 조회
    fun onGetMyPageSuccess(response: MyPageResponse)
    fun onGetMyPageFail(message: String)

    //마이페이지 수정
    fun onPutMyPageUpdateSuccess(response: BaseResponse)
    fun onPutMyPageUpdateFail(message: String)

    // 프로필 이미지 등록
    fun onPostProfileImageSuccess(response:SetProfileImageResponse)
    fun onPostProfileImageFailure(message: String)

    // 프로필 삭제
    fun onPatchProfileSuccess(response: BaseResponse)
    fun onPatchProfileFailure(message: String)



}