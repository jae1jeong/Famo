package com.makeus.jfive.famo.src.main.category

import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.category.models.CategoryInsertResponse

interface CategoryEditView {

    //카테고리 생성
    fun onPostCategoryInsertSuccess(response: CategoryInsertResponse)
    fun onPostCategoryInsertFail(message: String)

    //카테고리 삭제
    fun onDeleteCategoryDeleteSuccess(response: BaseResponse)
    fun onDeleteCategoryDeleteFail(message: String)

    //카테고리 수정
    fun onPatchCategoryUpdateSuccess(response: BaseResponse)
    fun onPatchCategoryUpdateFail(message: String)
}