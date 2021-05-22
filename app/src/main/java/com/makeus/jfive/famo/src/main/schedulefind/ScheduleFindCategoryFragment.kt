package com.makeus.jfive.famo.src.main.schedulefind

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lakue.pagingbutton.LakuePagingButton
import com.lakue.pagingbutton.OnPageSelectListener
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.MainActivity
import com.makeus.jfive.famo.src.main.schedulefind.adapter.*
import com.makeus.jfive.famo.src.main.schedulefind.models.*
import com.makeus.jfive.famo.src.main.today.models.MemoItem
import com.makeus.jfive.famo.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.makeus.jfive.famo.util.Constants
import com.makeus.jfive.famo.util.ScheduleDetailDialog
import kotlinx.android.synthetic.main.fragment_schedule_find_filter_bottom_dialog.*


class ScheduleFindCategoryFragment : Fragment(), CategoryInquiryView, CategoryFilterInterface,
    CategoryFilterView, ScheduleFindView, View.OnClickListener,
    SchedulefindFilterBottomDialogFragment.OnDialogButtonClickListener {

    private var schedulefindFilterBottomDialogFragment: SchedulefindFilterBottomDialogFragment? =
        null

    //페이징수
    var categorySchedulePagingCnt = 0

    //카테고리조회 페이징 수를 구하기 위한 변수
    var categoryPagintCnt = 0

    // 카테고리-필터 조회 4가지 구분을 위한 변수
    var leftPagintCnt = -1
    var donePagintCnt = -1
    var recentPagintCnt = -1
    var pickPagintCnt = -1

    //필터별 전체수를 위한 변수
    var totalCnt = 0

    //카테고리를 클릭한 것인지 확인하기 위한 변수
    var scheduleCategoryID = -1
    var searchWord = ""

    //클릭에 따른 체크표시 활성화를 위한 변수
    var remainCnt = 1
    var completionCnt = 1
    var recentsCnt = 1
    var bookmarkCnt = 1

    var categoryFilter: ImageView? = null
    var catogorySchedulePaging: LakuePagingButton? = null
    var recyclerviewScheduleFindCategory: RecyclerView? = null
    var scheduleFindCategoryFrameLayoutNoItem: FrameLayout? = null
    var categoryTextNoItem: TextView? = null
    var scheduleFindCategoryImageNoItem: ImageView? = null

    companion object{
        val categoryList: ArrayList<CategoryScheduleInquiryData> = arrayListOf()
        val categoryFilterList: ArrayList<CategoryFilterData> = arrayListOf()
        lateinit var categoryScheduleInquiryAdapter:CategoryScheduleInquiryAdapter
        lateinit var categoryFilterAdapter: CategoryFilterAdapter
    }
    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //setContentView 같다
        val view = inflater.inflate(R.layout.fragment_schedule_find_category, container, false)

        categoryFilter = view.findViewById(R.id.category_filter)
        catogorySchedulePaging = view.findViewById(R.id.category_schedule_paging)
        recyclerviewScheduleFindCategory =
            view.findViewById(R.id.recyclerview_schedule_find_category)
        scheduleFindCategoryFrameLayoutNoItem =
            view.findViewById(R.id.schedule_find_category_frame_layout_no_item)
        categoryTextNoItem = view.findViewById(R.id.category_text_no_item)
        scheduleFindCategoryImageNoItem = view.findViewById(R.id.schedule_find_category_image_no_item)

        var extra = this.arguments
        if (extra != null) {
            extra = arguments
            scheduleCategoryID = extra!!.getInt("scheduleCategoryID", -1)
            searchWord = extra!!.getString("searchWord").toString()


            Log.d("ScheduleFindCategoryFragment scheduleCategoryID", "값: $scheduleCategoryID")
            Log.d("ScheduleFindCategoryFragment searchWord", "값: $searchWord")
        }

        categoryFilter!!.setOnClickListener(this)

        scheduleFindCategoryImageNoItem!!.setOnClickListener {
            (activity as MainActivity).stateChangeBottomSheet(Constants.COLLASPE)
        }


        val word = ApplicationClass.sSharedPreferences.getString(Constants.SEARCHWROD, null)

        //검색
        if (word != null) {
            ScheduleFindService(this).tryGetScheduleSearch(word)
            val edit = ApplicationClass.sSharedPreferences.edit()
            edit.remove(Constants.SEARCHWROD)
            edit.apply()
        }

        //카테고리를 클릭 할때에만 조회
        if (scheduleCategoryID != -1) {

            if (categoryPagintCnt != 0) {
                CategoryInquiryService(this).tryGetCategoryInquiry(scheduleCategoryID, 0, 10)
            } else {
                CategoryInquiryService(this).tryGetCategoryInquiry(scheduleCategoryID, 0, 999)
            }
        }

        //한 번에 표시되는 버튼 수 (기본값 : 5)
        catogorySchedulePaging!!.setPageItemCount(4);
        catogorySchedulePaging!!.addBottomPageButton(categorySchedulePagingCnt, 1)

        //페이지 리스너를 클릭했을 때의 이벤트
        catogorySchedulePaging!!.setOnPageSelectListener(object : OnPageSelectListener {
            //PrevButton Click
            override fun onPageBefore(now_page: Int) {
                //prev 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                catogorySchedulePaging!!.addBottomPageButton(categorySchedulePagingCnt, now_page)
                CategoryInquiryService(this@ScheduleFindCategoryFragment)
                    .tryGetCategoryInquiry(scheduleCategoryID, ((now_page - 1) * 10), 10)

            }

            override fun onPageCenter(now_page: Int) {

                CategoryInquiryService(this@ScheduleFindCategoryFragment)
                    .tryGetCategoryInquiry(scheduleCategoryID, ((now_page - 1) * 10), 10)

            }

            //NextButton Click
            override fun onPageNext(now_page: Int) {
                //next 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                catogorySchedulePaging!!.addBottomPageButton(categorySchedulePagingCnt, now_page)
                CategoryInquiryService(this@ScheduleFindCategoryFragment)
                    .tryGetCategoryInquiry(scheduleCategoryID, ((now_page - 1) * 10), 10)
            }
        })

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryScheduleInquiryAdapter = CategoryScheduleInquiryAdapter(categoryList){}
        categoryFilterAdapter = CategoryFilterAdapter(categoryFilterList){}
    }

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
                categoryList.clear()
                if (categoryInquiryResponse.data.size > 0) {

                    if (categoryPagintCnt == 0) {

                        recyclerviewScheduleFindCategory!!.visibility = View.VISIBLE
                        scheduleFindCategoryFrameLayoutNoItem!!.visibility = View.GONE

                        val cnt = categoryInquiryResponse.data.size
                        //페이징수 세팅
                        if (cnt % 10 == 0) {
                            categorySchedulePagingCnt = cnt / 10
                        } else {
                            categorySchedulePagingCnt = (cnt / 10) + 1
                        }
                        catogorySchedulePaging!!.addBottomPageButton(categorySchedulePagingCnt, 1)
                        categoryPagintCnt++
                        CategoryInquiryService(this).tryGetCategoryInquiry(scheduleCategoryID, 0, 10)
                    }

                    if (categoryPagintCnt != 0) {

                        for (i in 0 until categoryInquiryResponse.data.size) {

                            if (categoryInquiryResponse.data[i].schedulePick == -1) {
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
                            } else {
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
                        }
                        recyclerviewScheduleFindCategory!!.layoutManager =
                            GridLayoutManager(
                                context, 2, GridLayoutManager.VERTICAL,
                                false
                            )
                        categoryScheduleInquiryAdapter = CategoryScheduleInquiryAdapter(categoryList) {
                            val detailDialog = ScheduleDetailDialog(context!!)
                            val scheduleItem = MemoItem(
                                    it.id,
                                    it.date,
                                    0,
                                    it.name,
                                    it.memo,
                                    false,
                                    null,
                                    null
                            ,0)
                            detailDialog.start(scheduleItem, null)
                            detailDialog.setOnModifyBtnClickedListener {
                                // 스케쥴 ID 보내기
                                val edit = ApplicationClass.sSharedPreferences.edit()
                                edit.putInt(Constants.EDIT_SCHEDULE_ID, it.id)
                                edit.apply()
                                Constants.IS_EDIT = true

                                //바텀 시트 다이얼로그 확장
                                (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
                            }
                        }
                        recyclerviewScheduleFindCategory!!.setHasFixedSize(true)
                        recyclerviewScheduleFindCategory!!.adapter =categoryScheduleInquiryAdapter

                    }

                }
                else{
                    recyclerviewScheduleFindCategory!!.visibility = View.GONE
                    scheduleFindCategoryFrameLayoutNoItem!!.visibility = View.VISIBLE
                    categoryTextNoItem!!.text = "관련 카테고리 일정이 없습니다."
                }


            }
            else -> {
                Log.d(
                    "TAG",
                    "onGetWholeScheduleInquirySuccess 100이 아닌: ${categoryInquiryResponse.message.toString()}")
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
                categoryFilterList.clear()

                //일정이 있으면
                if (response.data.size > 0) {

                    recyclerviewScheduleFindCategory!!.visibility = View.VISIBLE
                    scheduleFindCategoryFrameLayoutNoItem!!.visibility = View.GONE

                    //남은일정필터
                    if (leftPagintCnt % 4 == 1){
                        val filterCnt = response.data.size
                        //페이징수 세팅
                        if (filterCnt % 10 == 0) {
                            categorySchedulePagingCnt = filterCnt / 10
                        } else {
                            categorySchedulePagingCnt = (filterCnt / 10) + 1
                        }
                        catogorySchedulePaging!!.addBottomPageButton(categorySchedulePagingCnt, 1)

                        leftPagintCnt++
                        totalCnt++

                        CategoryFilterService(this).tryGetFilterCategoryInquiry(
                            scheduleCategoryID,
                            "left",
                            0,
                            10
                        )

                    }else if(totalCnt > 0){

                        if (response.data.size > 0) {

                            for (i in 0 until response.data.size) {

                                //즐겨찾기가 아닌경우
                                if (response.data[i].colorInfo != null) {
                                    categoryFilterList.add(
                                        CategoryFilterData(
                                            response.data[i].scheduleID,
                                            response.data[i].scheduleDate,
                                            response.data[i].scheduleName,
                                            response.data[i].scheduleMemo,
                                            response.data[i].schedulePick,
                                            response.data[i].colorInfo
                                        )
                                    )
                                } else if (response.data[i].colorInfo == null) {
                                    categoryFilterList.add(
                                        CategoryFilterData(
                                            response.data[i].scheduleID,
                                            response.data[i].scheduleDate,
                                            response.data[i].scheduleName,
                                            response.data[i].scheduleMemo,
                                            response.data[i].schedulePick,
                                            "#CED5D9"
                                        )
                                    )
                                }

                            }
                        }

                        recyclerviewScheduleFindCategory!!.layoutManager =
                            GridLayoutManager(
                                context, 2, GridLayoutManager.VERTICAL,
                                false
                            )
                        recyclerviewScheduleFindCategory!!.setHasFixedSize(true)
                        recyclerviewScheduleFindCategory!!.adapter = CategoryFilterAdapter(
                            categoryFilterList){

                            val detailDialog = ScheduleDetailDialog(context!!)
                            val scheduleItem = MemoItem(
                                it.scheduleID,
                                it.scheduleDate,
                                0,
                                it.scheduleName,
                                it.scheduleMemo,
                                false,
                                null,
                                null
                            ,0)
                            detailDialog.start(scheduleItem, null)
                            detailDialog.setOnModifyBtnClickedListener {
                                // 스케쥴 ID 보내기
                                val edit = ApplicationClass.sSharedPreferences.edit()
                                edit.putInt(Constants.EDIT_SCHEDULE_ID, it.scheduleID)
                                edit.apply()
                                Constants.IS_EDIT = true

                                //바텀 시트 다이얼로그 확장
                                (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
                            }
                        }
                        totalCnt--
                        leftPagintCnt--
                    }

                    //완료일정필터
                    if (donePagintCnt % 4 == 2){
                        val filterCnt = response.data.size
                        //페이징수 세팅
                        if (filterCnt % 10 == 0) {
                            categorySchedulePagingCnt = filterCnt / 10
                        } else {
                            categorySchedulePagingCnt = (filterCnt / 10) + 1
                        }
                        catogorySchedulePaging!!.addBottomPageButton(categorySchedulePagingCnt, 1)

                        donePagintCnt++
                        totalCnt++

                        CategoryFilterService(this).tryGetFilterCategoryInquiry(
                            scheduleCategoryID,
                            "done",
                            0,
                            10
                        )


                    }else if(totalCnt > 0){

                        if (response.data.size > 0) {

                            for (i in 0 until response.data.size) {

                                //즐겨찾기가 아닌경우
                                if (response.data[i].colorInfo != null) {
                                    categoryFilterList.add(
                                        CategoryFilterData(
                                            response.data[i].scheduleID,
                                            response.data[i].scheduleDate,
                                            response.data[i].scheduleName,
                                            response.data[i].scheduleMemo,
                                            response.data[i].schedulePick,
                                            response.data[i].colorInfo
                                        )
                                    )
                                } else if (response.data[i].colorInfo == null) {
                                    categoryFilterList.add(
                                        CategoryFilterData(
                                            response.data[i].scheduleID,
                                            response.data[i].scheduleDate,
                                            response.data[i].scheduleName,
                                            response.data[i].scheduleMemo,
                                            response.data[i].schedulePick,
                                            "#CED5D9"
                                        )
                                    )
                                }

                            }
                        }

                        recyclerviewScheduleFindCategory!!.layoutManager =
                            GridLayoutManager(
                                context, 2, GridLayoutManager.VERTICAL,
                                false
                            )
                        recyclerviewScheduleFindCategory!!.setHasFixedSize(true)
                        recyclerviewScheduleFindCategory!!.adapter = CategoryFilterAdapter(
                            categoryFilterList){

                            val detailDialog = ScheduleDetailDialog(context!!)
                            val scheduleItem = MemoItem(
                                it.scheduleID,
                                it.scheduleDate,
                                0,
                                it.scheduleName,
                                it.scheduleMemo,
                                false,
                                null,
                                null
                            ,0)
                            detailDialog.start(scheduleItem, null)
                            detailDialog.setOnModifyBtnClickedListener {
                                // 스케쥴 ID 보내기
                                val edit = ApplicationClass.sSharedPreferences.edit()
                                edit.putInt(Constants.EDIT_SCHEDULE_ID, it.scheduleID)
                                edit.apply()
                                Constants.IS_EDIT = true

                                //바텀 시트 다이얼로그 확장
                                (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
                            }

                        }
                        totalCnt--
                        donePagintCnt--
                    }


                    //최근일정필터
                    if (recentPagintCnt % 4 == 3){
                        val filterCnt = response.data.size
                        //페이징수 세팅
                        if (filterCnt % 10 == 0) {
                            categorySchedulePagingCnt = filterCnt / 10
                        } else {
                            categorySchedulePagingCnt = (filterCnt / 10) + 1
                        }
                        catogorySchedulePaging!!.addBottomPageButton(categorySchedulePagingCnt, 1)

                        recentPagintCnt++
                        totalCnt++

                        CategoryFilterService(this).tryGetFilterCategoryInquiry(
                            scheduleCategoryID,
                            "recent",
                            0,
                            10
                        )


                    }else if(totalCnt > 0){

                        if (response.data.size > 0) {

                            for (i in 0 until response.data.size) {

                                //즐겨찾기가 아닌경우
                                if (response.data[i].colorInfo != null) {
                                    categoryFilterList.add(
                                        CategoryFilterData(
                                            response.data[i].scheduleID,
                                            response.data[i].scheduleDate,
                                            response.data[i].scheduleName,
                                            response.data[i].scheduleMemo,
                                            response.data[i].schedulePick,
                                            response.data[i].colorInfo
                                        )
                                    )
                                } else if (response.data[i].colorInfo == null) {
                                    categoryFilterList.add(
                                        CategoryFilterData(
                                            response.data[i].scheduleID,
                                            response.data[i].scheduleDate,
                                            response.data[i].scheduleName,
                                            response.data[i].scheduleMemo,
                                            response.data[i].schedulePick,
                                            "#CED5D9"
                                        )
                                    )
                                }

                            }
                        }

                        recyclerviewScheduleFindCategory!!.layoutManager =
                            GridLayoutManager(
                                context, 2, GridLayoutManager.VERTICAL,
                                false
                            )
                        recyclerviewScheduleFindCategory!!.setHasFixedSize(true)
                        recyclerviewScheduleFindCategory!!.adapter = CategoryFilterAdapter(
                            categoryFilterList){

                            val detailDialog = ScheduleDetailDialog(context!!)
                            val scheduleItem = MemoItem(
                                it.scheduleID,
                                it.scheduleDate,
                                0,
                                it.scheduleName,
                                it.scheduleMemo,
                                false,
                                null,
                                null
                            ,0)
                            detailDialog.start(scheduleItem, null)
                            detailDialog.setOnModifyBtnClickedListener {
                                // 스케쥴 ID 보내기
                                val edit = ApplicationClass.sSharedPreferences.edit()
                                edit.putInt(Constants.EDIT_SCHEDULE_ID, it.scheduleID)
                                edit.apply()
                                Constants.IS_EDIT = true

                                //바텀 시트 다이얼로그 확장
                                (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
                            }

                        }
                        totalCnt--
                        recentPagintCnt--
                    }


                    //즐겨찾기일정필터
                    if (pickPagintCnt % 4 == 0){
                        val filterCnt = response.data.size
                        //페이징수 세팅
                        if (filterCnt % 10 == 0) {
                            categorySchedulePagingCnt = filterCnt / 10
                        } else {
                            categorySchedulePagingCnt = (filterCnt / 10) + 1
                        }
                        catogorySchedulePaging!!.addBottomPageButton(categorySchedulePagingCnt, 1)

                        pickPagintCnt++
                        totalCnt++

                        CategoryFilterService(this).tryGetFilterCategoryInquiry(
                            scheduleCategoryID,
                            "pick",
                            0,
                            10
                        )

                    }else if(totalCnt > 0){

                        if (response.data.size > 0) {

                            for (i in 0 until response.data.size) {

                                //즐겨찾기가 아닌경우
                                if (response.data[i].colorInfo != null) {
                                    categoryFilterList.add(
                                        CategoryFilterData(
                                            response.data[i].scheduleID,
                                            response.data[i].scheduleDate,
                                            response.data[i].scheduleName,
                                            response.data[i].scheduleMemo,
                                            response.data[i].schedulePick,
                                            response.data[i].colorInfo
                                        )
                                    )
                                } else if (response.data[i].colorInfo == null) {
                                    categoryFilterList.add(
                                        CategoryFilterData(
                                            response.data[i].scheduleID,
                                            response.data[i].scheduleDate,
                                            response.data[i].scheduleName,
                                            response.data[i].scheduleMemo,
                                            response.data[i].schedulePick,
                                            "#CED5D9"
                                        )
                                    )
                                }

                            }
                        }

                        recyclerviewScheduleFindCategory!!.layoutManager =
                            GridLayoutManager(
                                context, 2, GridLayoutManager.VERTICAL,
                                false
                            )
                        recyclerviewScheduleFindCategory!!.setHasFixedSize(true)
                        recyclerviewScheduleFindCategory!!.adapter = CategoryFilterAdapter(
                            categoryFilterList){

                            val detailDialog = ScheduleDetailDialog(context!!)
                            val scheduleItem = MemoItem(
                                it.scheduleID,
                                it.scheduleDate,
                                0,
                                it.scheduleName,
                                it.scheduleMemo,
                                false,
                                null,
                                null
                            ,0)
                            detailDialog.start(scheduleItem, null)
                            detailDialog.setOnModifyBtnClickedListener {
                                // 스케쥴 ID 보내기
                                val edit = ApplicationClass.sSharedPreferences.edit()
                                edit.putInt(Constants.EDIT_SCHEDULE_ID, it.scheduleID)
                                edit.apply()
                                Constants.IS_EDIT = true

                                //바텀 시트 다이얼로그 확장
                                (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
                            }

                        }
                        totalCnt--
                        pickPagintCnt--
                    }
                }
                //메모가 없으면
                else{
                    recyclerviewScheduleFindCategory!!.visibility = View.GONE
                    scheduleFindCategoryFrameLayoutNoItem!!.visibility = View.VISIBLE
                    categoryTextNoItem!!.text = "필터조회한 메모가 없습니다."
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
                Log.d("TAG", "onGetScheduleSearchSuccess: 검색 성공")

                val searchCnt = response.data.size

                if (response.data.size == 0) {
                    recyclerviewScheduleFindCategory!!.visibility = View.GONE
                    scheduleFindCategoryFrameLayoutNoItem!!.visibility = View.VISIBLE
                    categoryTextNoItem!!.text = "검색어에 맞는 메모가 없습니다."

                } else {
                    recyclerviewScheduleFindCategory!!.visibility = View.VISIBLE
                    scheduleFindCategoryFrameLayoutNoItem!!.visibility = View.GONE

                    //페이징수 세팅
                    if (searchCnt % 10 == 0) {
                        categorySchedulePagingCnt = searchCnt / 10
                    } else {
                        categorySchedulePagingCnt = (searchCnt / 10) + 1
                    }

                    catogorySchedulePaging!!.addBottomPageButton(categorySchedulePagingCnt, 1)

                    val searchList: ArrayList<ScheduleSearchData> = arrayListOf()

                    if (response.data.size > 0) {

                        for (i in 0 until response.data.size) {

                            //즐겨찾기가 아니면서 색상이 없는 경우
                            if (response.data[i].colorInfo != null) {
                                searchList.add(
                                    ScheduleSearchData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        response.data[i].scheduleDate,
                                        response.data[i].schedulePick,
                                        response.data[i].colorInfo
                                    )
                                )
                            } else if (response.data[i].colorInfo == null) {
                                searchList.add(
                                    ScheduleSearchData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        response.data[i].scheduleDate,
                                        response.data[i].schedulePick,
                                        "#CED5D9"
                                    )
                                )
                            }

                        }
                    }

                    recyclerviewScheduleFindCategory!!.layoutManager =
                        GridLayoutManager(
                            context, 2, GridLayoutManager.VERTICAL,
                            false
                        )
                    recyclerviewScheduleFindCategory!!.setHasFixedSize(true)
                    recyclerviewScheduleFindCategory!!.adapter = ScheduleSearchAdapter(searchList) {

                        val detailDialog = ScheduleDetailDialog(context!!)
                        val scheduleItem = MemoItem(
                            it.scheduleID,
                            it.scheduleDate,
                            0,
                            it.scheduleName,
                            it.scheduleMemo,
                            false,
                            null,
                            null
                        ,0)
                        detailDialog.start(scheduleItem, null)
                        detailDialog.setOnModifyBtnClickedListener {
                            // 스케쥴 ID 보내기
                            val edit = ApplicationClass.sSharedPreferences.edit()
                            edit.putInt(Constants.EDIT_SCHEDULE_ID, it.scheduleID)
                            edit.apply()
                            Constants.IS_EDIT = true

                            //바텀 시트 다이얼로그 확장
                            (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
                        }

                    }

                }


            }
            else -> {
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

//        val layoutInflater: LayoutInflater = activity!!.getSystemService(
//            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

//        val view: View =
//            layoutInflater.inflate(R.layout.fragment_schedule_find_filter_bottom_dialog, null);
//        val abc: ImageView = view.findViewById(R.id.filter_btn_remain_squre)

        when (view.id) {
            R.id.filter_btn_remain -> {
                leftPagintCnt = 1

                CategoryFilterService(this).tryGetFilterCategoryInquiry(
                    scheduleCategoryID,
                    "left",
                    0,
                    999
                )
                schedulefindFilterBottomDialogFragment!!.dismiss()
            }

            R.id.filter_btn_completion -> {
                donePagintCnt = 2

                CategoryFilterService(this).tryGetFilterCategoryInquiry(
                    scheduleCategoryID,
                    "done",
                    0,
                    999)
                schedulefindFilterBottomDialogFragment!!.dismiss()
            }

            R.id.filter_btn_recents -> {
                recentPagintCnt = 3
                CategoryFilterService(this).tryGetFilterCategoryInquiry(
                    scheduleCategoryID,
                    "recent",
                    0,
                    999
                )
                schedulefindFilterBottomDialogFragment!!.dismiss()
            }

            R.id.filter_btn_bookmark -> {
                pickPagintCnt = 0
                CategoryFilterService(this).tryGetFilterCategoryInquiry(
                    scheduleCategoryID,
                    "pick",
                    0,
                    999
                )
                schedulefindFilterBottomDialogFragment!!.dismiss()
            }


        }
    }
}