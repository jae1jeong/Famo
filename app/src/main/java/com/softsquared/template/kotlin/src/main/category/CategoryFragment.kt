package com.softsquared.template.kotlin.src.main.category

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentCategoryEditBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.adapter.ScheduleCategoryEditAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.ScheduleFindFragment
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleCategoryAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleCategoryData

class CategoryFragment : BaseFragment<FragmentCategoryEditBinding>(FragmentCategoryEditBinding::bind,
    R.layout.fragment_category_edit) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createCategoryRecyclerview()

        binding.categoryEditBtnPlus.setOnClickListener {

        }


        //X 버튼
        binding.categoryEditXBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(ScheduleFindFragment.newInstance());
        }

    }

    fun createCategoryRecyclerview() {
        //테스트 데이터
        val categoryEditList = arrayListOf(
            ScheduleCategoryData("학교"),
            ScheduleCategoryData("알바"),
            ScheduleCategoryData("친구")
        )

        binding.recyclerviewEditCategory.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerviewEditCategory.setHasFixedSize(true)
        binding.recyclerviewEditCategory.adapter = ScheduleCategoryEditAdapter(categoryEditList)
    }

    companion object {
        fun newInstance(): CategoryFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return CategoryFragment()
        }
    }
}