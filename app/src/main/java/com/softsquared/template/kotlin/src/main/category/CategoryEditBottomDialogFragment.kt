package com.softsquared.template.kotlin.src.main.category

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.src.main.category.adapter.ScheduleCategoryEditAdapter
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertRequest
import com.softsquared.template.kotlin.src.main.category.models.CategoryInsertResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData
import kotlinx.android.synthetic.main.activity_category_edit.*
import kotlinx.android.synthetic.main.activity_category_edit.category_color_1
import kotlinx.android.synthetic.main.fragment_category_add_bottom_dialog.*
import kotlinx.android.synthetic.main.fragment_category_add_bottom_dialog.view.*

class CategoryEditBottomDialogFragment : BottomSheetDialogFragment(),
    CategoryEditView {

    //bundle로 받기위한 변수
    var bundleColor: String? = null
    var size: Int? = null

    var bundleCheckColor : List<String>? = null

    var text = ""
    var color: Int? = null
    private lateinit var categoryEditAdapter: ScheduleCategoryEditAdapter

    var selectColor = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_category_color_bottom_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onPostCategoryInsertSuccess(response: CategoryInsertResponse) {
    }

    override fun onPostCategoryInsertFail(message: String) {
    }

    override fun onDeleteCategoryDeleteSuccess(response: BaseResponse) {
    }

    override fun onDeleteCategoryDeleteFail(message: String) {
    }

    override fun onPatchCategoryUpdateSuccess(response: BaseResponse) {
    }

    override fun onPatchCategoryUpdateFail(message: String) {
    }
}