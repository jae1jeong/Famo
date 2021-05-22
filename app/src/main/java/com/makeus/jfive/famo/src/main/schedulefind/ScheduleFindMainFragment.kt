package com.makeus.jfive.famo.src.main.schedulefind

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.lakue.pagingbutton.LakuePagingButton
import com.lakue.pagingbutton.OnPageSelectListener
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.src.main.MainActivity
import com.makeus.jfive.famo.src.main.schedulefind.adapter.*
import com.makeus.jfive.famo.src.main.schedulefind.models.*
import com.makeus.jfive.famo.src.main.today.models.MemoItem
import com.makeus.jfive.famo.src.wholeschedule.WholeScheduleActivity
import com.makeus.jfive.famo.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.makeus.jfive.famo.util.Constants
import com.makeus.jfive.famo.util.ScheduleDetailDialog
import kotlinx.android.synthetic.main.fragment_schedule_main_find.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ScheduleFindMainFragment : Fragment(), CategoryInquiryView, ScheduleFindView,
    IScheduleCategoryRecyclerView {

    //카테고리 편집으로 보내줄 변수
    var name = ""
    var color = ""
    var size = 0
    var categoryID = ""

    //전체페이징수
    var wholePagingCnt = 0

    var pagingCnt = 1

    private val partList: ArrayList<ScheduleWholeData> = arrayListOf()

    var recyclerviewWhole : RecyclerView? = null
    var scheduleFindtTvTotaySchedule : TextView? = null
    var scheduleFindBtnDetail : RelativeLayout? = null
    var scheduleFindPaging : LakuePagingButton? = null
    var scheduleFindViewPager : ViewPager? = null
    var scheduleFindTabLayout : TabLayout? = null
    var scheduleFindTvTotaySchedule : TextView? = null
    var scheduleFindName : TextView? = null
    var scheduleFindMainWholeLayoutNoItem : FrameLayout? = null
    var scheduleFindMainWholeImageNoItem : ImageView? = null


    companion object {
        lateinit var scheduleWholeAdapter: ScheduleWholeAdapter
        var wholeScheduleList: ArrayList<ScheduleWholeData> = arrayListOf()

    }

    //메인액티비티 oncreate랑 비슷하다
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //setContentView 같다
        val view = inflater.inflate(R.layout.fragment_schedule_main_find, container, false)

        recyclerviewWhole = view.findViewById(R.id.recyclerview_whole)
        scheduleFindtTvTotaySchedule = view.findViewById(R.id.schedule_find_tv_totay_schedule)
        scheduleFindBtnDetail = view.findViewById(R.id.schedule_find_btn_detail)
        scheduleFindPaging = view.findViewById(R.id.schedule_find_paging)
        scheduleFindViewPager = view.findViewById(R.id.schedule_find_view_pager)
        scheduleFindTabLayout = view.findViewById(R.id.schedule_find_tab_layout)
        scheduleFindtTvTotaySchedule = view.findViewById(R.id.schedule_find_tv_totay_schedule)
        scheduleFindName = view.findViewById(R.id.schedule_find_name)
        scheduleFindMainWholeLayoutNoItem = view.findViewById(R.id.schedule_find_main_whole_layout_no_item)
        scheduleFindMainWholeImageNoItem = view.findViewById(R.id.schedule_find_main_whole_image_no_item)

        scheduleFindTabLayout!!.setSelectedTabIndicatorColor(Color.parseColor("#242424")); // 밑줄색
        scheduleFindTabLayout!!.setSelectedTabIndicatorHeight(9); // 밑줄높이(두께)

        //전체일정수
        ScheduleFindService(this).tryGetWholeScheduleCount()
        //전체일정조회
        ScheduleFindService(this).tryGetWholeScheduleInquiry(0, 10)
        //남은일정
        ScheduleFindService(this).tryGetRestScheduleCount("today")

        val adapter = ScheduleFindPagerAdapter(childFragmentManager)
        adapter.addFragment(ScheduleFindBookmarkFragment(), "즐겨찾기")
        adapter.addFragment(ScheduleFindLatelyFragment(), "최근")
        Log.d("TAG", "ScheduleFindMainFragment onViewCreated: 뷰페이저")
        scheduleFindViewPager!!.adapter = adapter
        scheduleFindTabLayout!!.setupWithViewPager(scheduleFindViewPager)


        val name = ApplicationClass.sSharedPreferences.getString(
            Constants.USER_NICKNAME, null
        )

        //이름설정
        if (name != null) {
            scheduleFindName!!.text = name + "님,"
        }

        //앞으로 내보내기
        scheduleFindtTvTotaySchedule!!.bringToFront()

        //자세히 보기 클릭 시
        scheduleFindBtnDetail!!.setOnClickListener {
            val intent = Intent(activity, WholeScheduleActivity::class.java)
            startActivity(intent)
        }

        //메모가없을경우 이미지 클릭 시 메모작성
        scheduleFindMainWholeImageNoItem!!.setOnClickListener {
            (activity as MainActivity).stateChangeBottomSheet(Constants.COLLASPE)
        }


//        한 번에 표시되는 버튼 수 (기본값 : 5)
        scheduleFindPaging!!.setPageItemCount(4);

//        총 페이지 버튼 수와 현재 페이지 설정
        Log.d("TAG", "wholePagingCnt : ${wholePagingCnt} ")
        scheduleFindPaging!!.addBottomPageButton(10, 1);

//        페이지 리스너를 클릭했을 때의 이벤트
        scheduleFindPaging!!.setOnPageSelectListener(object : OnPageSelectListener {
            //PrevButton Click
            override fun onPageBefore(now_page: Int) {
                //prev 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                scheduleFindPaging!!.addBottomPageButton(wholePagingCnt, now_page)
                ScheduleFindService(this@ScheduleFindMainFragment).tryGetWholeScheduleInquiry(
                    ((now_page - 1) * 10), 10
                )
            }

            override fun onPageCenter(now_page: Int) {
                ScheduleFindService(this@ScheduleFindMainFragment).tryGetWholeScheduleInquiry(
                    ((now_page - 1) * 10), 10
                )

            }

            //NextButton Click
            override fun onPageNext(now_page: Int) {
                //next 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                scheduleFindPaging!!.addBottomPageButton(wholePagingCnt, now_page)
                ScheduleFindService(this@ScheduleFindMainFragment).tryGetWholeScheduleInquiry(
                    ((now_page - 1) * 10), 10
                )

            }
        })

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleWholeAdapter = ScheduleWholeAdapter(wholeScheduleList,this) {}
        shimmer_main_layout.startShimmerAnimation()
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            try{
                if(shimmer_main_layout.isAnimationStarted){
                    shimmer_main_layout.stopShimmerAnimation()
                    shimmer_main_layout.visibility = View.GONE
                    schedule_find_main_layout.visibility = View.VISIBLE
                }
            }catch (e: NullPointerException){

            }
        }
    }


    override fun onGetUserCategoryInquirySuccess(responseUser: UserCategoryInquiryResponse) {
    }

    override fun onGetUserCategoryInquiryFail(message: String) {
    }

    override fun onGetCategoryInquirySuccess(categoryInquiryResponse: CategoryInquiryResponse) {
    }

    override fun onGetCategoryInquiryFail(message: String) {
    }

    override fun onGetWholeScheduleInquirySuccess(response: WholeScheduleInquiryResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onGetWholeScheduleInquirySuccess 성공")
                wholeScheduleList.clear()

                if (response.data.size > 0) {

                    recyclerviewWhole!!.visibility = View.VISIBLE
                    scheduleFindMainWholeLayoutNoItem!!.visibility = View.GONE

                    for (i in 0 until response.data.size) {
                        //즐겨찾기 X and 카테고리 O인경우
                        if (response.data[i].colorInfo == null) {
                            wholeScheduleList.add(
                                ScheduleWholeData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleDate,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    response.data[i].schedulePick,
                                    response.data[i].scheduleStatus,
                                    "#CED5D9"
                                )
                            )
//                            "#CED5D9"
                            //즐겨찾기X and 카테고리O
                        } else if (response.data[i].colorInfo != null) {
                            wholeScheduleList.add(
                                ScheduleWholeData(
                                    response.data[i].scheduleID,
                                    response.data[i].scheduleDate,
                                    response.data[i].scheduleName,
                                    response.data[i].scheduleMemo,
                                    response.data[i].schedulePick,
                                    response.data[i].scheduleStatus,
                                    response.data[i].colorInfo
                                )
                            )
                        }

                    }

                    //전체일정 리사이큘러뷰 연결
                    recyclerviewWhole!!.layoutManager =
                        GridLayoutManager(
                            context, 2, GridLayoutManager.VERTICAL,
                            false
                        )
                    recyclerviewWhole!!.setHasFixedSize(true)

                    scheduleWholeAdapter = ScheduleWholeAdapter(wholeScheduleList, this) { it ->
                        val detailDialog = ScheduleDetailDialog(context!!)
                        val scheduleItem = MemoItem(
                            it.id,
                            it.date,
                            0,
                            it.name,
                            it.memo,
                            false,
                            null,
                            null, 0
                        )
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

                    recyclerviewWhole!!.adapter = scheduleWholeAdapter

                } else {
                    recyclerviewWhole!!.visibility = View.GONE
                    scheduleFindMainWholeLayoutNoItem!!.visibility = View.VISIBLE
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
                Log.d("TAG", "onPostBookmarkSuccess: 즐겨찾기 성공")
            }
            else -> {
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
                if (cnt % 10 == 0) {
                    wholePagingCnt = cnt / 10
                } else {
                    wholePagingCnt = (cnt / 10) + 1
                }

                Log.e("TAG", "onGetWholeScheduleCountSuccess: $wholePagingCnt")
                scheduleFindPaging!!.addBottomPageButton(wholePagingCnt, 1)

            }
            else -> {
                Log.d("TAG", "onPostBookmarkSuccess: 즐겨찾기 실패 ${response.message.toString()}")
            }
        }

    }

    override fun onGetWholeScheduleCountFailure(message: String) {
    }

    override fun onGetLatelyScheduleFindInquirySuccess(response: LatelyScheduleInquiryResponse) {
    }

    override fun onGetLatelySchedulefindInquiryFail(message: String) {
    }

    @SuppressLint("SetTextI18n")
    override fun onGetTodayRestScheduleSuccess(response: TodayRestScheduleResponse) {

        when (response.code) {
            100 -> {
                Log.d("TAG", "onGetTodayRestScheduleSuccess: 남은일정조회성공")
                scheduleFindtTvTotaySchedule!!.text = " 일정이 " +
                        response.data[0].remainScheduleCount.toString() +
                        "개 "

            }
            else -> {
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

    }

    override fun onGetScheduleSearchFail(message: String) {
    }


    override fun onItemMoveBtnClicked(scheduleCategoryID: Int) {
    }

    override fun onColor(): ArrayList<String> {

        val ab = ArrayList<String>()
        return ab
    }

    override fun onClickedTwice() {
    }


//    override fun onUpdate() {
//
//
//        childFragmentManager.beginTransaction()
//            .replace(R.id.schedule_find_main_fragment, ScheduleFindFragment())
//            .commit()
//    }


}