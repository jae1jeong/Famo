package com.softsquared.template.kotlin.src.main.mypage.edit

import com.softsquared.template.kotlin.src.main.mypage.models.MyPageEditResponse

interface MyPageEditView {

    //마이페이지 조회
    fun onGetMyPageEditSuccess(editResponse: MyPageEditResponse)
    fun onGetMyPageEditFail(message: String)

}