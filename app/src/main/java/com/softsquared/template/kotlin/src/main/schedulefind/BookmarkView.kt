package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.config.BaseResponse

interface BookmarkView {

    //일정찾기 - 즐겨찾기
    fun onPostBookmarkSuccess(response: BaseResponse)
    fun onPostBookmarkFail(message: String)


}