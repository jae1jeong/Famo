package com.softsquared.template.kotlin.src.main.schedulefind

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindCategoryBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.adapter.ScheduleCategoryEditAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.CategoryFilterAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.CategoryScheduleInquiryAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleWholeAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import com.softsquared.template.kotlin.util.Constants

class ScheduleFindCategoryFragment : BaseFragment<FragmentScheduleFindCategoryBinding>(
    FragmentScheduleFindCategoryBinding::bind, R.layout.fragment_schedule_find_category
), CategoryInquiryView, CategoryFilterInterface, CategoryFilterView {

    companion object {
        fun newInstance(): ScheduleFindCategoryFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return ScheduleFindCategoryFragment()
        }
    }

    var categoryID = 0
    var scheduleCategoryID = 0
//    private lateinit var scheduleWholeAdapter: ScheduleWholeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var extra = this.arguments
        if (extra != null) {
            extra = arguments
            categoryID = extra?.getInt("categoryID", 10)!!
            scheduleCategoryID = extra.getInt("scheduleCategoryID", 10)


            Log.d("ScheduleFindCategoryFragment categoryID", "값: $categoryID")
            Log.d("ScheduleFindCategoryFragment scheduleCategoryID", "값: $scheduleCategoryID")
        }

        CategoryInquiryService(this).tryGetCategoryInquiry(scheduleCategoryID, 0, 10)
//        CategoryInquiryService(this).tryGetUserCategoryInquiry()
//        createRecyclerview()

//        (activity as MainActivity).onBackPressed()

//        val num = ApplicationClass.sSharedPreferences.getString(Constants.NUM, null)
//        if (num != null){
//            CategoryFilterService(this).tryGetUserCategoryInquiry(scheduleCategoryID, "left", 0, 100)
//
//        }



        binding.categoryFilter.setOnClickListener {

            (activity as MainActivity).onMoveFilterFragment(scheduleCategoryID)
        }
    }

    override fun viewPagerApiRequest() {
        super.viewPagerApiRequest()
        // 카테고리
        CategoryInquiryService(this).tryGetCategoryInquiry(categoryID, 0, 10)
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
                    categoryList.add(
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

    override fun onFliter() {
        Log.d("TAG", "onFliter: 확인")
    }

    override fun onGetCategoryFilterInquirySuccess(response: CategoryFilterResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onGetCategoryFilterInquirySuccess: 필터조회성공")

                val categoryFilterList: ArrayList<CategoryFilterData> = arrayListOf()

                if (response.data.size > 0) {

                    for (i in 0 until response.data.size) {

                        //즐겨찾기가 아닌경우
                        if (response.data[i].schedulePick == -1) {
                            categoryFilterList.add(
                                CategoryFilterData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleDate,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    R.drawable.schedule_find_inbookmark,
                                    response.data[i].colorInfo
                                )
                            )
                            // 즐겨찾기 인 경우
                        } else {
                            categoryFilterList.add(
                                CategoryFilterData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleDate,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    R.drawable.schedule_find_bookmark,
                                    response.data[i].colorInfo
                                )
                            )
                        }

                    }
                }

                binding.recyclerviewScheduleFindCategory.layoutManager =
                    GridLayoutManager(
                        context, 2, GridLayoutManager.VERTICAL,
                        false
                    )
                binding.recyclerviewScheduleFindCategory.setHasFixedSize(true)
                binding.recyclerviewScheduleFindCategory.adapter = CategoryFilterAdapter(categoryFilterList)

                val edit = ApplicationClass.sSharedPreferences.edit()
                edit.remove("num")
                edit.apply()

            }
            else -> {
                Log.d(
                    "TAG",
                    "onGetCategoryFilterInquirySuccess: 남은일정조회성공 ${response.message.toString()}"
                )
            }
        }

    }

    override fun onGetCategoryFilterInquiryFail(message: String) {
    }
}