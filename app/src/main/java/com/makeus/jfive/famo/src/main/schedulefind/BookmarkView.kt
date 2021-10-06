package com.makeus.jfive.famo.src.main.schedulefind

import com.makeus.jfive.famo.config.BaseResponse

interface BookmarkView {

    //일정찾기 - 즐겨찾기
    fun onPostBookmarkSuccess(response: BaseResponse)
    fun onPostBookmarkFail(message: String)


}