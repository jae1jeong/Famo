package com.softsquared.template.kotlin.src.searchhistories

import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.searchhistories.models.SearchHistoriesResponse

interface SearchHistoriesView {

    //검색기록
    fun onGetSearchHistoriesSuccess(response: SearchHistoriesResponse)
    fun onGetSearchHistoriesFail(message: String)

    //검색기록삭제
    fun onDeleteSearchHistoriesSuccess(response: BaseResponse)
    fun onDeleteSearchHistoriesFail(message: String)
}