package com.softsquared.template.kotlin.src.main.mypage.edit

import com.softsquared.template.kotlin.src.main.mypage.models.MyPageEditCommentsResponse

interface MyPageEditView {

    //마이페이지 조회
    fun onGetMyPageEditSuccess(editCommentsResponse: MyPageEditCommentsResponse)
    fun onGetMyPageEditFail(message: String)

}