package com.softsquared.template.kotlin.src.main.mypage

import com.softsquared.template.kotlin.src.main.mypage.models.MyPageCommentsResponse

interface MyPageView {

    //상단멘트
    fun onGetMyPageCommentsSuccess(response: MyPageCommentsResponse)
    fun onGetMyPageCommentsFail(message: String)

}