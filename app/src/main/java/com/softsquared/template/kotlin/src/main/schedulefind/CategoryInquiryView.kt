package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse

interface CategoryInquiryView {

    //카테고리 조회
    fun onGetCategoryInquirySuccess(response: CategoryInquiryResponse)
    fun onGetCategoryInquiryFail(message: String)
}