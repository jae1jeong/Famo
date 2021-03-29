package com.softsquared.template.kotlin.src.wholeschedule.lately

import android.os.Bundle
import android.util.Log
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
import com.softsquared.template.kotlin.src.wholeschedule.lately.adapter.WholeScheduleLatelyAdapter
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse
import com.softsquared.template.kotlin.util.Constants
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TAG", "WholeLatelyScheduleFragment: 확인")

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
                    .tryGetLatelyScheduleInquiry(((now_page - 1)), 10)

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

    override fun viewPagerApiRequest() {
        super.viewPagerApiRequest()
        if (testCnt != 0){
            WholeLatelyScheduleService(this).tryGetLatelyScheduleInquiry(0,10)
        }else{
            WholeLatelyScheduleService(this).tryGetLatelyScheduleInquiry(0,999)
        }

    }

    override fun onGetLatelyScheduleInquirySuccess(response: LatelyScheduleInquiryResponse) {

        when(response.code){
            100 -> {
                showCustomToast("즐겨찾기 성공")
                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회성공")

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
                    testCnt++
                    WholeLatelyScheduleService(this).tryGetLatelyScheduleInquiry(0,10)

                }

                if (testCnt != 0){
                    val latelyListWhole: ArrayList<WholeScheduleLatelyData> = arrayListOf()

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
                        context, 2, GridLayoutManager.VERTICAL, false
                    )
                    binding.recyclerViewLately.setHasFixedSize(true)
                    binding.recyclerViewLately.adapter = WholeScheduleLatelyAdapter(latelyListWhole) {
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
                        )
                        detailDialog.start(scheduleItem,null)
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
                showCustomToast("즐겨찾기 실패")
                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회실패 - ${response.message.toString()}")
            }
        }
    }

    override fun onGetLatelyScheduleInquiryFail(message: String) {
    }
}