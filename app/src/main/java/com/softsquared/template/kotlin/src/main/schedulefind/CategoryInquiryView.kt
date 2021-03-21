package com.softsquared.template.kotlin.src.main.schedulefind

import com.softsquared.template.kotlin.src.main.schedulefind.models.CategoryInquiryResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.UserCategoryInquiryResponse

interface CategoryInquiryView {

    // 유저별 카테고리 조회
    fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse)
    fun onGetUserCategoryInquiryFail(message: String)

    // 카테고리 조회
    fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse)
    fun onGetCategoryInquiryFail(message: String)
}