package com.makeus.jfive.famo.src.searchhistories

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.searchhistories.models.SearchHistoriesResponse

interface SearchHistoriesView {

    //검색기록
    fun onGetSearchHistoriesSuccess(response: SearchHistoriesResponse)
    fun onGetSearchHistoriesFail(message: String)

    //검색기록삭제
    fun onDeleteSearchHistoriesSuccess(response: BaseResponse)
    fun onDeleteSearchHistoriesFail(message: String)
}