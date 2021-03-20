package com.softsquared.template.kotlin.src.main.schedulefind

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindCategoryBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.adapter.ScheduleCategoryEditAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.CategoryScheduleInquiryAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleWholeAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.*

class ScheduleFindCategoryFragment : BaseFragment<FragmentScheduleFindCategoryBinding>(
    FragmentScheduleFindCategoryBinding::bind, R.layout.fragment_schedule_find_category
), CategoryInquiryView {

    companion object {
        fun newInstance(): ScheduleFindCategoryFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return ScheduleFindCategoryFragment()
        }
    }

    var categoryID = ""
//    private lateinit var scheduleWholeAdapter: ScheduleWholeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var extra = this.arguments
        if (extra != null) {
            extra = arguments
            categoryID = extra?.getInt("categoryID", 10).toString()
            Log.d("ScheduleFindCategoryFragment categoryID", "값: $categoryID")
        }
        CategoryInquiryService(this).tryGetCategoryInquiry(Integer.parseInt(categoryID), 1, 1)
//        CategoryInquiryService(this).tryGetUserCategoryInquiry()
//        createRecyclerview()

//        (activity as MainActivity).onBackPressed()

        binding.categoryFilter.setOnClickListener {

            (activity as MainActivity).onMoveFilterFragment()
        }
    }

    override fun viewPagerApiRequest() {
        super.viewPagerApiRequest()
        // 카테고리
        CategoryInquiryService(this).tryGetCategoryInquiry(Integer.parseInt(categoryID), 1, 1)


    }

    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {
        Log.d("TAG", "55555555555: 유져벌카테고일정조회 성공")
    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {
        Log.d("TAG", "onGetCategoryInquirySuccess: $categoryInquiryResponse")
        Log.d("TAG", "1111111111")

        when (categoryInquiryResponse.code) {
            100 -> {
                Log.d("TAG", "onGetCategoryInquirySuccess 성공")
                //테스트 데이터
                var categoryList: ArrayList<CategoryScheduleInquiryData> = arrayListOf()
                for (i in 0 until categoryInquiryResponse.data.size) {
                    categoryList = arrayListOf(
                        CategoryScheduleInquiryData(
                            categoryInquiryResponse.data[i].scheduleID,
                            categoryInquiryResponse.data[i].scheduleDate,
                            categoryInquiryResponse.data[i].scheduleName,
                            categoryInquiryResponse.data[i].scheduleMemo,
                            categoryInquiryResponse.data[i].schedulePick,
                            categoryInquiryResponse.data[i].colorInfo
                        )
                    )
                }
                //전체일정 리사이큘러뷰 연결
                binding.recyclerviewScheduleFindCategory.layoutManager =
                    GridLayoutManager(
                        context, 2, GridLayoutManager.VERTICAL,
                        false
                    )
                binding.recyclerviewScheduleFindCategory.setHasFixedSize(true)
                binding.recyclerviewScheduleFindCategory.adapter =
                    CategoryScheduleInquiryAdapter(categoryList)

            }
            else -> {
                Log.d(
                    "TAG",
                    "onGetWholeScheduleInquirySuccess 100이 아닌: ${categoryInquiryResponse.message.toString()}"
                )
            }
        }

    }

    override fun onGetCategoryInquiryFail(message: String) {
    }
}