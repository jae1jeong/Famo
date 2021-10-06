package com.makeus.jfive.famo.src.domain.use_case.get_user_category

import com.makeus.jfive.famo.src.domain.model.main.CategoryDto
import com.makeus.jfive.famo.src.domain.repository.MainRepository
import com.makeus.jfive.famo.src.main.schedulefind.models.UserCategoryInquiryResponse
import io.reactivex.Observable
import javax.inject.Inject

class GetUserCategoryUseCase @Inject constructor(
    private val mainRepo:MainRepository
) {
    fun execute(): Observable<CategoryDto>{
        return mainRepo.getUserCategoryInquiry()
    }
}