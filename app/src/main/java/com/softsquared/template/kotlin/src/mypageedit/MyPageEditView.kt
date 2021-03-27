package com.softsquared.template.kotlin.src.mypageedit

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.mypageedit.models.MyPageCommentsResponse
import com.softsquared.template.kotlin.src.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.mypageedit.models.SetProfileImageResponse

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




}