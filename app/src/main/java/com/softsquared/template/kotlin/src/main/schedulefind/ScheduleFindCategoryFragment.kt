package com.softsquared.template.kotlin.src.main.schedulefind

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.lakue.pagingbutton.OnPageSelectListener
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindCategoryBinding
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.*
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.softsquared.template.kotlin.util.Constants
import kotlinx.android.synthetic.main.fragment_schedule_find_filter_bottom_dialog.*


class ScheduleFindCategoryFragment : BaseFragment<FragmentScheduleFindCategoryBinding>(
    FragmentScheduleFindCategoryBinding::bind, R.layout.fragment_schedule_find_category
), CategoryInquiryView, CategoryFilterInterface, CategoryFilterView, ScheduleFindView,
    View.OnClickListener, SchedulefindFilterBottomDialogFragment.OnDialogButtonClickListener{

    companion object {
        fun newInstance(): ScheduleFindCategoryFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return ScheduleFindCategoryFragment()
        }
    }

    private var schedulefindFilterBottomDialogFragment: SchedulefindFilterBottomDialogFragment? =
        null

    //전체페이징수
    var categorySchedulePagingCnt = 0

    // 카테고리/필터 조회시 전체수를 위한 변수
    var categoryPagintCnt = 0
    var recentPagintCnt = 0
    var leftPagintCnt = 0
    var pickPagintCnt = 0
    var donePagintCnt = 0

    //카테고리를 클릭한 것인지 확인하기 위한 변수
    var scheduleCategoryID = -1
    var searchWord = ""

    //클릭에 따른 체크표시 활성화를 위한 변수
    var remainCnt = 1
    var completionCnt = 1
    var recentsCnt = 1
    var bookmarkCnt = 1


    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var extra = this.arguments
        if (extra != null) {
            extra = arguments
//            categoryID = extra?.getInt("categoryID", 10)!!
            scheduleCategoryID = extra!!.getInt("scheduleCategoryID", -1)
            searchWord = extra!!.getString("searchWord").toString()


//            Log.d("ScheduleFindCategoryFragment categoryID", "값: $categoryID")
            Log.d("ScheduleFindCategoryFragment scheduleCategoryID", "값: $scheduleCategoryID")
            Log.d("ScheduleFindCategoryFragment searchWord", "값: $searchWord")
        }

        binding.categoryFilter.setOnClickListener(this)

        //        필터
//        binding.categoryFilter.setOnClickListener {
//
//            (activity as MainActivity).onMoveFilterFragment(scheduleCategoryID)
//        }


        val word = ApplicationClass.sSharedPreferences.getString(Constants.SEARCHWROD, null)

        //검색
        if (word != null){
            ScheduleFindService(this).tryGetScheduleSearch(word)
        }

        //카테고리를 클릭 할때에만 조회
        if (scheduleCategoryID != -1){

            if (categoryPagintCnt != 0){
                CategoryInquiryService(this).tryGetCategoryInquiry(scheduleCategoryID, 0, 2)
            }else{
                CategoryInquiryService(this).tryGetCategoryInquiry(scheduleCategoryID, 0, 999)
            }
        }

        //한 번에 표시되는 버튼 수 (기본값 : 5)
        binding.catogorySchedulePaging.setPageItemCount(2);
        binding.catogorySchedulePaging.addBottomPageButton(categorySchedulePagingCnt, 1)

        //페이지 리스너를 클릭했을 때의 이벤트
        binding.catogorySchedulePaging.setOnPageSelectListener(object : OnPageSelectListener {
            //PrevButton Click
            override fun onPageBefore(now_page: Int) {
                //prev 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.catogorySchedulePaging.addBottomPageButton(categorySchedulePagingCnt, now_page)
                CategoryInquiryService(this@ScheduleFindCategoryFragment)
                    .tryGetCategoryInquiry(scheduleCategoryID, ((now_page - 1) * 2), 2)

            }

            override fun onPageCenter(now_page: Int) {

                CategoryInquiryService(this@ScheduleFindCategoryFragment)
                    .tryGetCategoryInquiry(scheduleCategoryID, ((now_page - 1) * 2), 2)

            }

            //NextButton Click
            override fun onPageNext(now_page: Int) {
                //next 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.catogorySchedulePaging.addBottomPageButton(categorySchedulePagingCnt, now_page)
                CategoryInquiryService(this@ScheduleFindCategoryFragment)
                    .tryGetCategoryInquiry(scheduleCategoryID, ((now_page - 1) * 2), 2)
            }
        })


    }

//    override fun viewPagerApiRequest() {
//        super.viewPagerApiRequest()
//        // 카테고리
//        CategoryInquiryService(this).tryGetCategoryInquiry(categoryID, 0, 10)
//    }

    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {
        Log.d("TAG", "55555555555: 유져벌카테고일정조회 성공")
    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {
        Log.d("TAG", "onGetCategoryInquirySuccess: $categoryInquiryResponse")

        when (categoryInquiryResponse.code) {
            100 -> {
                Log.d("TAG", "onGetCategoryInquirySuccess 성공")

                if (categoryPagintCnt == 0){

                    val cnt = categoryInquiryResponse.data.size
                    //페이징수 세팅
                    if (cnt % 2 == 0) {
                        categorySchedulePagingCnt = cnt / 2
                    } else {
                        categorySchedulePagingCnt = (cnt / 2) + 1
                    }
                    binding.catogorySchedulePaging.addBottomPageButton(categorySchedulePagingCnt, 1)
                    categoryPagintCnt++
                    CategoryInquiryService(this).tryGetCategoryInquiry(scheduleCategoryID, 0, 2)
                }

                if (categoryPagintCnt != 0 ){
                    val categoryList: ArrayList<CategoryScheduleInquiryData> = arrayListOf()

                    for (i in 0 until categoryInquiryResponse.data.size) {

                        if (categoryInquiryResponse.data[i].schedulePick == -1) {
                            categoryList.add(
                                CategoryScheduleInquiryData(
                                    categoryInquiryResponse.data[i].scheduleID,
                                    categoryInquiryResponse.data[i].scheduleDate,
                                    categoryInquiryResponse.data[i].scheduleName,
                                    categoryInquiryResponse.data[i].scheduleMemo,
                                    R.drawable.schedule_find_inbookmark,
                                    categoryInquiryResponse.data[i].colorInfo
                                )
                            )
                        } else {
                            categoryList.add(
                                CategoryScheduleInquiryData(
                                    categoryInquiryResponse.data[i].scheduleID,
                                    categoryInquiryResponse.data[i].scheduleDate,
                                    categoryInquiryResponse.data[i].scheduleName,
                                    categoryInquiryResponse.data[i].scheduleMemo,
                                    R.drawable.schedule_find_bookmark,
                                    categoryInquiryResponse.data[i].colorInfo
                                )
                            )
                        }
                    }
                    binding.recyclerviewScheduleFindCategory.layoutManager =
                        GridLayoutManager(context, 2, GridLayoutManager.VERTICAL,
                            false)
                    binding.recyclerviewScheduleFindCategory.setHasFixedSize(true)
                    binding.recyclerviewScheduleFindCategory.adapter =
                        CategoryScheduleInquiryAdapter(categoryList)
                }

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

                //남은
                if (leftPagintCnt == 0){
                    val filterCnt = response.data.size
                    //페이징수 세팅
                    if (filterCnt % 10 == 0) {
                        categorySchedulePagingCnt = filterCnt / 10
                    } else {
                        categorySchedulePagingCnt = (filterCnt / 10) + 1
                    }
                    binding.catogorySchedulePaging.addBottomPageButton(categorySchedulePagingCnt, 1)
                    leftPagintCnt++
                    CategoryFilterService(this).tryGetFilterCategoryInquiry(scheduleCategoryID, "left", 0, 10)
                }

                if (leftPagintCnt != 0){
                    val categoryFilterList: ArrayList<CategoryFilterData> = arrayListOf()

                    if (response.data.size > 0) {

                        for (i in 0 until response.data.size) {

                            //즐겨찾기가 아닌경우
                            if (response.data[i].schedulePick == -1 && response.data[i].colorInfo != null) {
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
                            }
                            else if(response.data[i].schedulePick == -1 && response.data[i].colorInfo == null) {
                                categoryFilterList.add(
                                    CategoryFilterData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleDate,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        R.drawable.schedule_find_inbookmark,
                                        "#CED5D9"
                                    )
                                )
                            }
                            else if(response.data[i].schedulePick == 1 && response.data[i].colorInfo == null){
                                categoryFilterList.add(
                                    CategoryFilterData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleDate,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        R.drawable.schedule_find_bookmark,
                                        "#CED5D9"
                                    )
                                )
                            }
                            else if(response.data[i].schedulePick == 1 && response.data[i].colorInfo != null){
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
                    binding.recyclerviewScheduleFindCategory.adapter = CategoryFilterAdapter(
                        categoryFilterList
                    )
                }

                //완료
                if (donePagintCnt == 0){
                    val filterCnt = response.data.size
                    //페이징수 세팅
                    if (filterCnt % 10 == 0) {
                        categorySchedulePagingCnt = filterCnt / 10
                    } else {
                        categorySchedulePagingCnt = (filterCnt / 10) + 1
                    }
                    binding.catogorySchedulePaging.addBottomPageButton(categorySchedulePagingCnt, 1)
                    donePagintCnt++
                    CategoryFilterService(this).tryGetFilterCategoryInquiry(scheduleCategoryID, "done", 0, 10)
                }

                if (donePagintCnt != 0){
                    val categoryFilterList: ArrayList<CategoryFilterData> = arrayListOf()

                    if (response.data.size > 0) {

                        for (i in 0 until response.data.size) {

                            //즐겨찾기가 아닌경우
                            if (response.data[i].schedulePick == -1 && response.data[i].colorInfo != null) {
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
                            }
                            else if(response.data[i].schedulePick == -1 && response.data[i].colorInfo == null) {
                                categoryFilterList.add(
                                    CategoryFilterData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleDate,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        R.drawable.schedule_find_inbookmark,
                                        "#CED5D9"
                                    )
                                )
                            }
                            else if(response.data[i].schedulePick == 1 && response.data[i].colorInfo == null){
                                categoryFilterList.add(
                                    CategoryFilterData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleDate,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        R.drawable.schedule_find_bookmark,
                                        "#CED5D9"
                                    )
                                )
                            }
                            else if(response.data[i].schedulePick == 1 && response.data[i].colorInfo != null){
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
                    binding.recyclerviewScheduleFindCategory.adapter = CategoryFilterAdapter(
                        categoryFilterList
                    )
                }

                //최신
                if (recentPagintCnt == 0){
                    val filterCnt = response.data.size
                    //페이징수 세팅
                    if (filterCnt % 10 == 0) {
                        categorySchedulePagingCnt = filterCnt / 10
                    } else {
                        categorySchedulePagingCnt = (filterCnt / 10) + 1
                    }
                    binding.catogorySchedulePaging.addBottomPageButton(categorySchedulePagingCnt, 1)
                    recentPagintCnt++
                    CategoryFilterService(this).tryGetFilterCategoryInquiry(scheduleCategoryID, "recent", 0, 10)
                }

                if (recentPagintCnt != 0){
                    val categoryFilterList: ArrayList<CategoryFilterData> = arrayListOf()

                    if (response.data.size > 0) {

                        for (i in 0 until response.data.size) {

                            if (response.data[i].schedulePick == -1 && response.data[i].colorInfo != null) {
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
                            }
                            else if(response.data[i].schedulePick == -1 && response.data[i].colorInfo == null) {
                                categoryFilterList.add(
                                    CategoryFilterData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleDate,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        R.drawable.schedule_find_inbookmark,
                                        "#CED5D9"
                                    )
                                )
                            }
                            else if(response.data[i].schedulePick == 1 && response.data[i].colorInfo == null){
                                categoryFilterList.add(
                                    CategoryFilterData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleDate,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        R.drawable.schedule_find_bookmark,
                                        "#CED5D9"
                                    )
                                )
                            }
                            else if(response.data[i].schedulePick == 1 && response.data[i].colorInfo != null){
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
                    binding.recyclerviewScheduleFindCategory.adapter = CategoryFilterAdapter(
                        categoryFilterList
                    )
                }

                //즐겨찾기
                if (pickPagintCnt == 0){
                    val filterCnt = response.data.size
                    //페이징수 세팅
                    if (filterCnt % 10 == 0) {
                        categorySchedulePagingCnt = filterCnt / 10
                    } else {
                        categorySchedulePagingCnt = (filterCnt / 10) + 1
                    }
                    binding.catogorySchedulePaging.addBottomPageButton(categorySchedulePagingCnt, 1)
                    pickPagintCnt++
                    CategoryFilterService(this).tryGetFilterCategoryInquiry(scheduleCategoryID, "left", 0, 10)
                }

                if (pickPagintCnt != 0){
                    val categoryFilterList: ArrayList<CategoryFilterData> = arrayListOf()

                    if (response.data.size > 0) {

                        for (i in 0 until response.data.size) {

                            //즐겨찾기가 아닌경우
                            if (response.data[i].schedulePick == -1 && response.data[i].colorInfo != null) {
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
                            }
                            else if(response.data[i].schedulePick == -1 && response.data[i].colorInfo == null) {
                                categoryFilterList.add(
                                    CategoryFilterData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleDate,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        R.drawable.schedule_find_inbookmark,
                                        "#CED5D9"
                                    )
                                )
                            }
                            else if(response.data[i].schedulePick == 1 && response.data[i].colorInfo == null){
                                categoryFilterList.add(
                                    CategoryFilterData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleDate,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        R.drawable.schedule_find_bookmark,
                                        "#CED5D9"
                                    )
                                )
                            }
                            else if(response.data[i].schedulePick == 1 && response.data[i].colorInfo != null){
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
                    binding.recyclerviewScheduleFindCategory.adapter = CategoryFilterAdapter(
                        categoryFilterList
                    )
                }
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

    override fun onGetWholeScheduleInquirySuccess(response: WholeScheduleInquiryResponse) {
    }

    override fun onGetWholeScheduleInquiryFail(message: String) {
    }

    override fun onPostBookmarkSuccess(response: BaseResponse) {
    }

    override fun onPostBookmarkFail(message: String) {
    }

    override fun onGetWholeScheduleCountSuccess(response: WholeScheduleCountResponse) {
    }

    override fun onGetWholeScheduleCountFailure(message: String) {
    }

    override fun onGetLatelyScheduleFindInquirySuccess(response: LatelyScheduleInquiryResponse) {
    }

    override fun onGetLatelySchedulefindInquiryFail(message: String) {
    }

    override fun onGetTodayRestScheduleSuccess(response: TodayRestScheduleResponse) {
    }

    override fun onGetTodayRestScheduleFail(message: String) {
    }

    @SuppressLint("CommitPrefEdits")
    override fun onGetScheduleSearchSuccess(response: ScheduleSearchResponse) {

        val edit = ApplicationClass.sSharedPreferences.edit()
        edit.putString(Constants.SEARCH_CNT, response.data.size.toString())
        edit.apply()

        when (response.code) {
            100 -> {
                showCustomToast("검색 성공")
                Log.d("TAG", "onGetScheduleSearchSuccess: 검색 성공")

                val searchCnt = response.data.size
                //페이징수 세팅
                if (searchCnt % 10 == 0) {
                    categorySchedulePagingCnt = searchCnt / 10
                } else {
                    categorySchedulePagingCnt = (searchCnt / 10) + 1
                }

                binding.catogorySchedulePaging.addBottomPageButton(categorySchedulePagingCnt, 1)

                val searchList: ArrayList<ScheduleSearchData> = arrayListOf()

                if (response.data.size > 0) {

                    for (i in 0 until response.data.size) {

                        //즐겨찾기가 아니면서 색상이 없는 경우
                        if (response.data[i].schedulePick == -1 && response.data[i].colorInfo != null) {
                            searchList.add(
                                ScheduleSearchData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    response.data[i].scheduleDate,
                                    R.drawable.schedule_find_inbookmark,
                                    response.data[i].colorInfo
                                )
                            )
                        }
                        else if(response.data[i].schedulePick == -1 && response.data[i].colorInfo == null){
                            searchList.add(
                                ScheduleSearchData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    response.data[i].scheduleDate,
                                    R.drawable.schedule_find_inbookmark,
                                    "#CED5D9"
                                )
                            )
                        }
                        else if (response.data[i].schedulePick == 1 && response.data[i].colorInfo == null){
                            searchList.add(
                                ScheduleSearchData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    response.data[i].scheduleDate,
                                    R.drawable.schedule_find_bookmark,
                                    "#CED5D9"
                                )
                            )
                        }
                        else if(response.data[i].schedulePick == 1 && response.data[i].colorInfo != null){
                            searchList.add(
                                ScheduleSearchData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    response.data[i].scheduleDate,
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
                binding.recyclerviewScheduleFindCategory.adapter = ScheduleSearchAdapter(searchList)
            }
            else -> {
                showCustomToast("검색 성공")
                Log.d("TAG", "onGetScheduleSearchSuccess: 검색 실패 ${response.message.toString()}")
            }
        }

    }

    override fun onGetScheduleSearchFail(message: String) {
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.category_filter -> {
                schedulefindFilterBottomDialogFragment =
                    SchedulefindFilterBottomDialogFragment()
                schedulefindFilterBottomDialogFragment!!.setOnDialogButtonClickListener(this)
                schedulefindFilterBottomDialogFragment!!.isCancelable = true
                schedulefindFilterBottomDialogFragment!!.show(
                    childFragmentManager,
                    schedulefindFilterBottomDialogFragment!!.tag
                )
//                (activity as MainActivity).onMoveFilterFragment(scheduleCategoryID)
            }
        }
    }

    @SuppressLint("InflateParams")
    override fun onDialogButtonClick(view: View) {
        when (view.id) {
            R.id.filter_btn_remain -> {
                Toast.makeText(activity, "남은", Toast.LENGTH_SHORT).show()

                if (leftPagintCnt != 0){
                    CategoryFilterService(this).tryGetFilterCategoryInquiry(scheduleCategoryID, "left", 0, 10)
                }else{
                    CategoryFilterService(this).tryGetFilterCategoryInquiry(scheduleCategoryID, "left", 0, 999)
                }
                schedulefindFilterBottomDialogFragment!!.dismiss()
            }

            R.id.filter_btn_completion -> {
                Toast.makeText(activity, "완료", Toast.LENGTH_SHORT).show()

                if (donePagintCnt != 0){
                    CategoryFilterService(this).tryGetFilterCategoryInquiry(scheduleCategoryID, "done", 0, 10)
                }else{

                    CategoryFilterService(this).tryGetFilterCategoryInquiry(scheduleCategoryID, "done", 0, 999)
                }
                schedulefindFilterBottomDialogFragment!!.dismiss()
            }

            R.id.filter_btn_recents -> {
                Toast.makeText(activity, "최신", Toast.LENGTH_SHORT).show()

                if (recentPagintCnt != 0){
                    CategoryFilterService(this).tryGetFilterCategoryInquiry(scheduleCategoryID, "recent", 0, 2)
                }else{
                    CategoryFilterService(this).tryGetFilterCategoryInquiry(scheduleCategoryID, "recent", 0, 999)
                }
                schedulefindFilterBottomDialogFragment!!.dismiss()
            }

            R.id.filter_btn_bookmark -> {
                Toast.makeText(activity, "즐겨찾기", Toast.LENGTH_SHORT).show()

                if (pickPagintCnt != 0){
                    CategoryFilterService(this).tryGetFilterCategoryInquiry(scheduleCategoryID, "pick", 0, 10)
                }else{
                    CategoryFilterService(this).tryGetFilterCategoryInquiry(scheduleCategoryID, "pick", 0, 999)
                }
                schedulefindFilterBottomDialogFragment!!.dismiss()
            }


        }
    }
}