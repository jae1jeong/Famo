package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryFilterResponse

interface CategoryFilterView {

    //카테고리별일정정렬조회
    fun onGetCategoryFilterInquirySuccess(response: CategoryFilterResponse)
    fun onGetCategoryFilterInquiryFail(message: String)
}