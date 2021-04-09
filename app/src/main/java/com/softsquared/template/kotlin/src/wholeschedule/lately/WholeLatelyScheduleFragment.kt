package com.softsquared.template.kotlin.src.wholeschedule.lately

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.lakue.pagingbutton.OnPageSelectListener
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindLatelyBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleLatelyData
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.src.wholeschedule.WholeScheduleActivity
import com.softsquared.template.kotlin.src.wholeschedule.lately.adapter.WholeScheduleLatelyAdapter
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.softsquared.template.kotlin.util.Constants
import com.softsquared.template.kotlin.util.MovieItemDecoration
import com.softsquared.template.kotlin.util.ScheduleDetailDialog

class WholeLatelyScheduleFragment : BaseFragment<FragmentScheduleFindLatelyBinding>(
    FragmentScheduleFindLatelyBinding::bind, R.layout.fragment_schedule_find_lately),
    WholeLatelyScheduleView {

    var latelySchedulePagingCnt = 0
    var testCnt = 0

    companion object {
        fun newInstance(): WholeLatelyScheduleFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return WholeLatelyScheduleFragment()
        }
        val latelyListWhole: ArrayList<WholeScheduleLatelyData> = arrayListOf()
        lateinit var wholeScheduleLatelyAdapter:WholeScheduleLatelyAdapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TAG", "WholeLatelyScheduleFragment: 확인")
        // 사이즈가 0인 경우는 초기화
        wholeScheduleLatelyAdapter = WholeScheduleLatelyAdapter(latelyListWhole){}

        //일정이 없을 떄 클릭 시 작성
//        binding.scheduelFindLatelyImageNoItem.setOnClickListener {
//            (activity as WholeScheduleActivity).stateChangeBottomSheet(Constants.COLLASPE)
//        }

        //한 번에 표시되는 버튼 수 (기본값 : 5)
        binding.wholeLatelySchedulePaging.setPageItemCount(4);
//        binding.wholeLatelySchedulePaging.addBottomPageButton(latelySchedulePagingCnt, 1)

        //페이지 리스너를 클릭했을 때의 이벤트
        binding.wholeLatelySchedulePaging.setOnPageSelectListener(object : OnPageSelectListener {
            //PrevButton Click
            override fun onPageBefore(now_page: Int) {
                //prev 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.wholeLatelySchedulePaging.addBottomPageButton(latelySchedulePagingCnt, now_page)
                WholeLatelyScheduleService(this@WholeLatelyScheduleFragment)
                    .tryGetLatelyScheduleInquiry(((now_page - 1)*10), 10)

            }
            override fun onPageCenter(now_page: Int) {
//                binding.recyclerviewWhole.visibility = View.GONE
//                setFrag(Integer.parseInt(now_page.toString())-1)

//                for (i in 1 until 4) {
//                    if (now_page == i) {
//                        ScheduleFindService(this@ScheduleFindFragment).tryGetWholeScheduleInquiry(
//                            ((i - 1) * 10), 10)
//                    }
//                }
                WholeLatelyScheduleService(this@WholeLatelyScheduleFragment)
                    .tryGetLatelyScheduleInquiry(((now_page - 1)*10), 10)

            }

            //NextButton Click
            override fun onPageNext(now_page: Int) {
                //next 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.wholeLatelySchedulePaging.addBottomPageButton(latelySchedulePagingCnt, now_page)
                WholeLatelyScheduleService(this@WholeLatelyScheduleFragment)
                    .tryGetLatelyScheduleInquiry(((now_page - 1)*10), 10)

            }
        })

    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
            .toInt()
    }

    override fun viewPagerApiRequest() {
        super.viewPagerApiRequest()
        if (testCnt != 0){
            WholeLatelyScheduleService(this).tryGetLatelyScheduleInquiry(0,10)
        }else{
            showLoadingDialog(context!!)
            WholeLatelyScheduleService(this).tryGetLatelyScheduleInquiry(0,9999)
        }

    }

    override fun onGetLatelyScheduleInquirySuccess(response: LatelyScheduleInquiryResponse) {

        when(response.code){
            100 -> {
                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회성공")
                latelyListWhole.clear()

                if (response.data.size == 0) {
                    binding.recyclerViewLately.visibility = View.GONE
                    binding.scheduleFindLatelyFrameLayoutNoItem.visibility = View.VISIBLE
                    binding.wholeLatelyTvItem.text = "최근생성한 메모가 없습니다.\n이곳에서는 작성이 불가능합니다."
                }

                if (response.data.size > 0){

                    if (testCnt == 0){

                        val cnt = response.data.size
                        //페이징수 세팅
                        if (cnt % 10 == 0) {
                            latelySchedulePagingCnt = cnt / 10
                        } else {
                            latelySchedulePagingCnt = (cnt / 10) + 1
                        }
                        Log.d("TAG", "onGetLatelyScheduleInquirySuccess: $latelySchedulePagingCnt")
                        binding.wholeLatelySchedulePaging.addBottomPageButton(latelySchedulePagingCnt, 1)

                        var size5 = 0
                        size5 = dpToPx(context!!, 10)

                        var size10 = 0
                        size10 = dpToPx(context!!, 3)

                        binding.recyclerViewLately.addItemDecoration(MovieItemDecoration(size10,size5))
                        testCnt++
                        WholeLatelyScheduleService(this).tryGetLatelyScheduleInquiry(0,10)

                    }

                    if (testCnt != 0){

                        for (i in 0 until response.data.size) {

                            if (response.data[i].schedulePick == -1 && response.data[i].colorInfo != null) {
                                latelyListWhole.add(
                                    WholeScheduleLatelyData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleDate,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        R.drawable.schedule_find_inbookmark,
                                        response.data[i].categoryID, response.data[i].colorInfo
                                    )
                                )

                            } else if (response.data[i].schedulePick == -1 && response.data[i].colorInfo == null) {
                                latelyListWhole.add(
                                    WholeScheduleLatelyData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleDate,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        R.drawable.schedule_find_inbookmark,
                                        response.data[i].categoryID,
                                        "#CED5D9"
                                    )
                                )

                            } else if (response.data[i].schedulePick == 1 && response.data[i].colorInfo == null) {
                                latelyListWhole.add(
                                    WholeScheduleLatelyData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleDate,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        R.drawable.schedule_find_bookmark,
                                        response.data[i].categoryID,
                                        "#CED5D9"
                                    )
                                )

                            } else if (response.data[i].schedulePick == 1 && response.data[i].colorInfo != null) {
                                latelyListWhole.add(
                                    WholeScheduleLatelyData(
                                        response.data[i].scheduleID,
                                        response.data[i].scheduleDate,
                                        response.data[i].scheduleName,
                                        response.data[i].scheduleMemo,
                                        R.drawable.schedule_find_bookmark,
                                        response.data[i].categoryID,
                                        response.data[i].colorInfo
                                    )
                                )
                            }
                        }

                        // 즐겨찾기/최근 일정 리사이클러뷰 연결
                        binding.recyclerViewLately.layoutManager = GridLayoutManager(
                            context, 2, GridLayoutManager.VERTICAL, false)
                        wholeScheduleLatelyAdapter = WholeScheduleLatelyAdapter(latelyListWhole) {
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
                            detailDialog.start(scheduleItem,null)
                            detailDialog.setOnModifyBtnClickedListener {
                                // 스케쥴 ID 보내기
                                val edit = ApplicationClass.sSharedPreferences.edit()
                                edit.putInt(Constants.EDIT_SCHEDULE_ID, it.scheduleID)
                                edit.apply()
                                Constants.IS_EDIT = true

                                //바텀 시트 다이얼로그 확장
                                (activity as WholeScheduleActivity).stateChangeBottomSheet(Constants.EXPAND)
                            }
                        }
//                    binding.recyclerViewLately.setHasFixedSize(true)
                        binding.recyclerViewLately.adapter = wholeScheduleLatelyAdapter
                    }
                }


            }
            else -> {
                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회실패 - ${response.message.toString()}")
            }
        }
        dismissLoadingDialog()
    }

    override fun onGetLatelyScheduleInquiryFail(message: String) {
    }
}