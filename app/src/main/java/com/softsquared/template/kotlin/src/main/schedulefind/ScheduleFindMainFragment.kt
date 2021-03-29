package com.softsquared.template.kotlin.src.main.schedulefind

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lakue.pagingbutton.OnPageSelectListener
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindBinding
import com.softsquared.template.kotlin.databinding.FragmentScheduleMainFindBinding
import com.softsquared.template.kotlin.src.main.AddMemoView
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.CategoryEditActivity
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.*
import com.softsquared.template.kotlin.src.main.schedulefind.models.*
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.src.searchhistories.ScheduleSearchActivity
import com.softsquared.template.kotlin.src.wholeschedule.WholeScheduleActivity
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.softsquared.template.kotlin.util.Constants
import com.softsquared.template.kotlin.util.ScheduleDetailDialog
import java.util.*
import kotlin.collections.ArrayList

class ScheduleFindMainFragment() : BaseFragment<FragmentScheduleMainFindBinding>
    (FragmentScheduleMainFindBinding::bind, R.layout.fragment_schedule_main_find),
    IScheduleCategoryRecyclerView, CategoryInquiryView, ScheduleFindView,
    ScheduleBookmarkView {

    //카테고리 편집으로 보내줄 변수
    var name = ""
    var color = ""
    var size = 0
    var categoryID = ""

    //전체페이징수
    var wholePagingCnt = 0

    var pagingCnt = 1

    private val partList: ArrayList<ScheduleWholeData> = arrayListOf()

    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        //전체일정수
        ScheduleFindService(this).tryGetWholeScheduleCount()
        //전체일정조회
        ScheduleFindService(this).tryGetWholeScheduleInquiry(0, 4)
        //남은일정
        ScheduleFindService(this).tryGetRestScheduleCount("today")

        val adapter = ScheduleFindPagerAdapter(childFragmentManager)
        adapter.addFragment(ScheduleFindBookmarkFragment(), "즐겨찾기")
        adapter.addFragment(ScheduleFindLatelyFragment(), "최근")
        Log.d("TAG", "onViewCreated: 뷰페이저")
        binding.scheduleFindViewPager.adapter = adapter
        binding.scheduleFindTabLayout.setupWithViewPager(binding.scheduleFindViewPager)

        //앞으로 내보내기
        binding.scheduleFindTvTotaySchedule.bringToFront()

        val token =
            ApplicationClass.sSharedPreferences.getString(ApplicationClass.X_ACCESS_TOKEN, null)
                .toString()
        Log.d("TAG", "일정찾기 홈 $token")


        //자세히 보기 클릭 시
        binding.scheduleFindBtnDetail.setOnClickListener {
            val intent = Intent(activity, WholeScheduleActivity::class.java)
            startActivity(intent)
        }

        //한 번에 표시되는 버튼 수 (기본값 : 5)
        binding.scheduleFindPaging.setPageItemCount(2);

        //총 페이지 버튼 수와 현재 페이지 설정
//        Log.d("TAG", "wholePagingCnt : ${wholePagingCnt} ")
//        binding.scheduleFindPaging.addBottomPageButton(10, 1);

        //페이지 리스너를 클릭했을 때의 이벤트
        binding.scheduleFindPaging.setOnPageSelectListener(object : OnPageSelectListener {
            //PrevButton Click
            override fun onPageBefore(now_page: Int) {
                //prev 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.scheduleFindPaging.addBottomPageButton(wholePagingCnt, now_page)
                ScheduleFindService(this@ScheduleFindMainFragment).tryGetWholeScheduleInquiry(
                    ((now_page - 1) * 4), 4
                )
            }

            override fun onPageCenter(now_page: Int) {

                ScheduleFindService(this@ScheduleFindMainFragment).tryGetWholeScheduleInquiry(
                    ((now_page - 1) * 4), 4
                )

            }

            //NextButton Click
            override fun onPageNext(now_page: Int) {
                //next 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.scheduleFindPaging.addBottomPageButton(wholePagingCnt, now_page)
                ScheduleFindService(this@ScheduleFindMainFragment).tryGetWholeScheduleInquiry(
                    ((now_page - 1) * 4), 4
                )

            }
        })

    }

    override fun viewPagerApiRequest() {
        super.viewPagerApiRequest()
        //최근일정
//        ScheduleFindService(this).tryGetLatelyScheduleFindInquiry(0, 2)
//        //즐겨찾기
//        ScheduleBookmarkService(this).tryGetScheduleBookmark(0, 2)
//
//        ScheduleFindService(this).tryGetWholeScheduleCount()
//        //전체일정
//        ScheduleFindService(this).tryGetWholeScheduleInquiry(0, 4)


    }


    //카테고리 클릭 시 카테고리별 일정으로 이동
    override fun onItemMoveBtnClicked(scheduleCategoryID: Int) {

        val scheduleFindCategoryFragment = ScheduleFindCategoryFragment()
        val bundle = Bundle()
//        bundle.putInt("categoryID", position)
        bundle.putInt("scheduleCategoryID", scheduleCategoryID)
        scheduleFindCategoryFragment.arguments = bundle
        childFragmentManager.beginTransaction()
            .replace(R.id.schedule_find_main_fragment, scheduleFindCategoryFragment)
            .commit()
    }

    override fun onMoveFilterFragment(scheduleCategoryID: Int) {
        val scheduleFindFilterBottomDialogBinding =
            SchedulefindFilterBottomDialogFragment()
        scheduleFindFilterBottomDialogBinding.show(
            fragmentManager!!, scheduleFindFilterBottomDialogBinding.tag
        )
    }

    //클릭 시 카테고리 색상변경을 위한 카테고리 색상을 가져와서 분배하는 작업
    //어댑터에서 color값을 가져오기위한 함수
    override fun onColor(): ArrayList<String> {

        var size = 0
        var colorList: List<String>? = null
        val colorStrList = ArrayList<String>()
        val colorID = ArrayList<Int>()
        colorList = color.split(":")

        for (i in color.indices) {
            if (color.substring(i, i + 1) == ":") {
                size++
            }
        }
        Log.d("로그", "사이즈 : $size")

        for (i in 0 until size) {

            if (colorList[i] == "#FF8484") {
                colorStrList!!.add("#FF8484")
                colorID.add(1)
            }
            if (colorList[i] == "#FCBC71") {
                colorStrList!!.add("#FCBC71")
                colorID.add(2)
            }
            if (colorList[i] == "#FCDC71") {
                colorStrList!!.add("#FCDC71")
                colorID.add(3)
            }
            if (colorList[i] == "#C6EF84") {
                colorStrList!!.add("#C6EF84")
                colorID.add(4)
            }
            if (colorList[i] == "#7ED391") {
                colorStrList!!.add("#7ED391")
                colorID.add(5)
            }
            if (colorList[i] == "#93EAD9") {
                colorStrList!!.add("#93EAD9")
                colorID.add(6)
            }
            if (colorList[i] == "#7CC3FF") {
                colorStrList!!.add("#7CC3FF")
                colorID.add(7)
            }
            if (colorList[i] == "#6D92F7") {
                colorStrList!!.add("#6D92F7")
                colorID.add(8)
            }
            if (colorList[i] == "#AB93FA") {
                colorStrList!!.add("#AB93FA")
                colorID.add(9)
            }
            if (colorList[i] == "#FFA2BE") {
                colorStrList!!.add("#FFA2BE")
                colorID.add(10)
            }

        }

        return colorStrList
    }

    //유저별 카테고리조회 성공
    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {
    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {

    }

    override fun onGetCategoryInquiryFail(message: String) {
    }

    //전체일정조회 성공
    override fun onGetWholeScheduleInquirySuccess(response: WholeScheduleInquiryResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onGetWholeScheduleInquirySuccess 성공")
//                Toast.makeText(activity,"전체일정조회",Toast.LENGTH_SHORT).show()

                val wholeScheduleList: ArrayList<ScheduleWholeData> = arrayListOf()

                if (response.data.size > 0) {

                    for (i in 0 until response.data.size) {
                        //즐겨찾기 X and 카테고리 O인경우
                        if (response.data[i].schedulePick == -1 && response.data[i].colorInfo == null) {
                            wholeScheduleList.add(
                                ScheduleWholeData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleDate,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    R.drawable.schedule_find_inbookmark,
                                    response.data[i].scheduleStatus,
                                    "#CED5D9"
                                )
                            )
//                            "#CED5D9"
                            //즐겨찾기X and 카테고리O
                        } else if (response.data[i].schedulePick == -1 && response.data[i].colorInfo != null) {
                            wholeScheduleList.add(
                                ScheduleWholeData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleDate,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    R.drawable.schedule_find_inbookmark,
                                    response.data[i].scheduleStatus,
                                    response.data[i].colorInfo
                                )
                            )
                        }
                        //즐겨찾기O and 카테고리 X
                        else if (response.data[i].schedulePick == 1 && response.data[i].colorInfo == null) {
                            wholeScheduleList.add(
                                ScheduleWholeData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleDate,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    R.drawable.schedule_find_bookmark,
                                    response.data[i].scheduleStatus,
                                    "#CED5D9"
                                )
                            )
                        }
                        //즐겨찾기 O and 카테고리 O
                        else if (response.data[i].schedulePick == 1 && response.data[i].colorInfo != null) {
                            wholeScheduleList.add(
                                ScheduleWholeData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleDate,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    R.drawable.schedule_find_bookmark,
                                    response.data[i].scheduleStatus,
                                    response.data[i].colorInfo
                                )
                            )
                        }
                    }
                }

                //전체일정 리사이큘러뷰 연결
                binding.recyclerviewWhole.layoutManager =
                    GridLayoutManager(
                        context, 2, GridLayoutManager.VERTICAL,
                        false
                    )
                binding.recyclerviewWhole.setHasFixedSize(true)
                binding.recyclerviewWhole.adapter = ScheduleWholeAdapter(wholeScheduleList){ it ->
                    val detailDialog = ScheduleDetailDialog(context!!)
                    val scheduleItem = MemoItem(
                        it.id,
                        it.date,
                        0,
                        it.name,
                        it.memo,
                        false,
                        null
                    )
                    detailDialog.start(scheduleItem)
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


            }
            else -> {
                Log.d(
                    "TAG",
                    "onGetWholeScheduleInquirySuccess 100이 아닌: ${response.message.toString()}"
                )
            }
        }

    }

    override fun onGetWholeScheduleInquiryFail(message: String) {
    }

    override fun onPostBookmarkSuccess(response: BaseResponse) {

        when (response.code) {
            100 -> {
                showCustomToast("즐겨찾기 성공")
                Log.d("TAG", "onPostBookmarkSuccess: 즐겨찾기 성공")
            }
            else -> {
                showCustomToast("즐겨찾기 실패")
                Log.d("TAG", "onPostBookmarkSuccess: 즐겨찾기 실패 ${response.message.toString()}")
            }
        }
    }

    override fun onPostBookmarkFail(message: String) {
    }

    override fun onGetWholeScheduleCountSuccess(response: WholeScheduleCountResponse) {

        when (response.code) {
            100 -> {
                val cnt: Int = response.totaldata[0].totalScheduleCount
                val cnt2: Int = response.totaldonedata[0].totalScheduleCount
                Log.d("TAG", "onGetWholeScheduleCountSuccess - 전체일정수 - $cnt")
                Log.d("TAG", "onGetWholeScheduleCountSuccess - 전체 해낸일정수 - $cnt2")

                //페이징수 세팅
                if (cnt % 4 == 0) {
                    wholePagingCnt = cnt / 4
                } else {
                    wholePagingCnt = (cnt / 4) + 1
                }

                Log.e("TAG", "onGetWholeScheduleCountSuccess: $wholePagingCnt", )
                binding.scheduleFindPaging.addBottomPageButton(wholePagingCnt, 1)

            }
            else -> {
                Log.d("TAG", "onPostBookmarkSuccess: 즐겨찾기 실패 ${response.message.toString()}")
            }
        }
    }

    override fun onGetWholeScheduleCountFailure(message: String) {
    }

    override fun onGetLatelyScheduleFindInquirySuccess(response: LatelyScheduleInquiryResponse) {

//        when(response.code){
//            100 -> {
//                showCustomToast("즐겨찾기 성공")
//                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회성공")
//
//                val latelyList = arrayListOf(
//                    ScheduleBookmarkData("최근제목", "최근시간"),
//                    ScheduleBookmarkData("최근제목", "최근시간")
//                )
//
//                // 즐겨찾기/최근 일정 리사이클러뷰 연결
//                binding.recyclerViewLately.layoutManager = LinearLayoutManager(
//                    context, LinearLayoutManager.VERTICAL, false
//                )
//                binding.recyclerViewLately.setHasFixedSize(true)
//                binding.recyclerViewLately.adapter = ScheduleBookmarkAdapter(latelyList)
//
//            }
//            else -> {
//                showCustomToast("즐겨찾기 실패")
//                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회실패 - ${response.message.toString()}")
//            }
//        }

    }

    override fun onGetLatelySchedulefindInquiryFail(message: String) {
    }

    @SuppressLint("SetTextI18n")
    override fun onGetTodayRestScheduleSuccess(response: TodayRestScheduleResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onGetTodayRestScheduleSuccess: 남은일정조회성공")
                binding.scheduleFindTvTotaySchedule.text = " 일정이 " +
                        response.data[0].remainScheduleCount.toString() +
                        "개 "

            }
            else -> {
                showCustomToast("즐겨찾기 실패")
                Log.d(
                    "TAG",
                    "onGetTodayRestScheduleSuccess: 남은일정조회성공 ${response.message.toString()}"
                )
            }
        }

    }

    override fun onGetTodayRestScheduleFail(message: String) {
    }

    override fun onGetScheduleSearchSuccess(response: ScheduleSearchResponse) {

        when (response.code) {
            100 -> {
                showCustomToast("검색 성공")
                Log.d("TAG", "onGetScheduleSearchSuccess: 검색 성공")
            }
            else -> {
                showCustomToast("검색 성공")
                Log.d("TAG", "onGetScheduleSearchSuccess: 검색 실패 ${response.message.toString()}")
            }
        }
    }

    override fun onGetScheduleSearchFail(message: String) {
    }

    override fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse) {

        when (response.code) {
            100 -> {
                showCustomToast("즐겨찾기 일정조회성공")
                Log.d("TAG", "onGetScheduleBookmarkSuccess: 즐겨찾기 일정조회성공")

                val boomarkList: ArrayList<WholeScheduleBookmarkData> = arrayListOf()

                for (i in 0 until response.data.size) {
                    boomarkList.add(
                        WholeScheduleBookmarkData(
                            response.data[i].scheduleID,
                            response.data[i].scheduleDate,
                            response.data[i].scheduleName,
                            response.data[i].scheduleMemo,
                            response.data[i].schedulePick,
                            10,
                            "#CED5D9"
                        )
                    )
                }

//                binding.recyclerViewBookmark.layoutManager = LinearLayoutManager(
//                    context, LinearLayoutManager.VERTICAL, false
//                )
//                binding.recyclerViewBookmark.setHasFixedSize(true)
//                binding.recyclerViewBookmark.adapter = ScheduleBookmarkAdapter(boomarkList)
//                scheduleCategoryAdapter.notifyDataSetChanged()


            }
            else -> {
                showCustomToast("즐겨찾기 실패")
                Log.d(
                    "TAG",
                    "onGetScheduleBookmarkSuccess: 즐겨찾기 일정조회성공 ${response.message.toString()}"
                )
            }
        }

    }

    override fun onGetScheduleBookmarkFail(message: String) {
    }

    override fun onResume() {
        super.onResume()

    }

}