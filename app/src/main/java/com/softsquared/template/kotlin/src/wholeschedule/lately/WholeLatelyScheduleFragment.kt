package com.softsquared.template.kotlin.src.wholeschedule.lately

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lakue.pagingbutton.OnPageSelectListener
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindLatelyBinding
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleLatelyAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleLatelyData
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse

class WholeLatelyScheduleFragment : BaseFragment<FragmentScheduleFindLatelyBinding>(
    FragmentScheduleFindLatelyBinding::bind, R.layout.fragment_schedule_find_lately),
    WholeLatelyScheduleView {

    var LatelySchedulePagingCnt = 0

    companion object {
        fun newInstance(): WholeLatelyScheduleFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return WholeLatelyScheduleFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        WholeLatelyScheduleService(this).tryGetLatelyScheduleInquiry(0,2)

        //한 번에 표시되는 버튼 수 (기본값 : 5)
        binding.wholeLatelySchedulePaging.setPageItemCount(4);

        //페이지 리스너를 클릭했을 때의 이벤트
        binding.wholeLatelySchedulePaging.setOnPageSelectListener(object : OnPageSelectListener {
            //PrevButton Click
            override fun onPageBefore(now_page: Int) {
                //prev 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.wholeLatelySchedulePaging.addBottomPageButton(10, now_page)
                WholeLatelyScheduleService(this@WholeLatelyScheduleFragment)
                    .tryGetLatelyScheduleInquiry(((now_page - 1)), 1)

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
                    .tryGetLatelyScheduleInquiry(((now_page - 1)), 1)

            }

            //NextButton Click
            override fun onPageNext(now_page: Int) {
                //next 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.wholeLatelySchedulePaging.addBottomPageButton(10, now_page)
                WholeLatelyScheduleService(this@WholeLatelyScheduleFragment)
                    .tryGetLatelyScheduleInquiry(((now_page - 1)), 1)

            }
        })



    }


    private fun createLatelyRecyclerview() {
        //테스트 데이터
//        val latelyList = arrayListOf(
//            ScheduleBookmarkData("최근제목", "최근시간"),
//            ScheduleBookmarkData("최근제목", "최근시간")
//        )
//
//        // 즐겨찾기/최근 일정 리사이클러뷰 연결
//        binding.recyclerViewLately.layoutManager = LinearLayoutManager(
//            context, LinearLayoutManager.VERTICAL, false
//        )
//        binding.recyclerViewLately.setHasFixedSize(true)
//        binding.recyclerViewLately.adapter = ScheduleBookmarkAdapter(latelyList)
    }

    override fun onGetLatelyScheduleInquirySuccess(response: LatelyScheduleInquiryResponse) {

        when(response.code){
            100 -> {
                showCustomToast("즐겨찾기 성공")
                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회성공")

                LatelySchedulePagingCnt = (response.data.size / 10) + 1
                binding.wholeLatelySchedulePaging.addBottomPageButton(LatelySchedulePagingCnt, 1)

                val latelyListWhole: ArrayList<WholeScheduleLatelyData> = arrayListOf()

                for (i in 0 until response.data.size) {

                    if (response.data[i].schedulePick == -1) {
                        latelyListWhole.add(
                            WholeScheduleLatelyData(
                                response.data[i].scheduleID,
                                response.data[i].scheduleDate,
                                response.data[i].scheduleName,
                                response.data[i].scheduleMemo,
                                R.drawable.schedule_find_inbookmark,
                                response.data[i].categoryID
                                ,response.data[i].colorInfo
                            )
                        )
                        // 즐겨찾기 인 경우
                    } else {
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
                binding.recyclerViewLately.layoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.VERTICAL, false
                )
                binding.recyclerViewLately.setHasFixedSize(true)
                binding.recyclerViewLately.adapter = ScheduleLatelyAdapter(latelyListWhole)

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