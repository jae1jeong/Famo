package com.softsquared.template.kotlin.src.main.mypage.edit

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageCommentsResponse
import com.softsquared.template.kotlin.src.main.mypage.models.MyPageResponse

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



}