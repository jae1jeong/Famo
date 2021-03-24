package com.softsquared.template.kotlin.src.wholeschedule.bookmark

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lakue.pagingbutton.OnPageSelectListener
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindBookmarkBinding
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkResponse
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleBookmarkData
import com.softsquared.template.kotlin.src.main.schedulefind.models.WholeScheduleLatelyData
import com.softsquared.template.kotlin.src.wholeschedule.bookmark.adapter.WholeScheduleBookmarkAdapter
import java.util.ArrayList

class WholeScheduleBookmarkFragment : BaseFragment<FragmentScheduleFindBookmarkBinding>(
    FragmentScheduleFindBookmarkBinding::bind, R.layout.fragment_schedule_find_bookmark),
    WholeBookmarkScheduleView{

    var bookmarkSchedulePagingCnt = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var extra = this.arguments
//        if (extra != null) {
//            extra = arguments
//            check = extra!!.getBoolean("boolean")
//            Log.d("ScheduleFindBookmarkFragment", "ㅁㅁㅁㅁㅁㅁㅁㅁ")
//            Log.d("ScheduleFindBookmarkFragment", "check: $check")
//        }

        WholeBookmarkScheduleService(this).tryGetScheduleBookmark(0,1)

        //한 번에 표시되는 버튼 수 (기본값 : 5)
        binding.wholeBookmarkSchedulePaging.setPageItemCount(4);
        binding.wholeBookmarkSchedulePaging.addBottomPageButton(4, 1)

        //페이지 리스너를 클릭했을 때의 이벤트
        binding.wholeBookmarkSchedulePaging.setOnPageSelectListener(object : OnPageSelectListener {
            //PrevButton Click
            override fun onPageBefore(now_page: Int) {
                //prev 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.wholeBookmarkSchedulePaging.addBottomPageButton(4, now_page)
                WholeBookmarkScheduleService(this@WholeScheduleBookmarkFragment)
                    .tryGetScheduleBookmark(((now_page - 1)), 1)

            }

            override fun onPageCenter(now_page: Int) {
                WholeBookmarkScheduleService(this@WholeScheduleBookmarkFragment)
                    .tryGetScheduleBookmark(((now_page - 1)*1), 1)

            }

            //NextButton Click
            override fun onPageNext(now_page: Int) {
                //next 버튼을 클릭하면 버튼이 재설정되고 버튼이 그려집니다.
                binding.wholeBookmarkSchedulePaging.addBottomPageButton(4, now_page)
                WholeBookmarkScheduleService(this@WholeScheduleBookmarkFragment)
                    .tryGetScheduleBookmark(((now_page - 1)*1), 1)

            }
        })

    }


    override fun onGetScheduleBookmarkSuccess(response: ScheduleBookmarkResponse) {

        when(response.code){
            100 -> {
                showCustomToast("즐겨찾기 성공")
                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회성공")

                bookmarkSchedulePagingCnt = (response.data.size / 10) + 1
//                binding.wholeBookmarkSchedulePaging.addBottomPageButton(4, 1)

                val bookmarkListWhole: ArrayList<WholeScheduleBookmarkData> = arrayListOf()

                for (i in 0 until response.data.size) {

                    if (response.data[i].schedulePick == -1) {
                        bookmarkListWhole.add(
                            WholeScheduleBookmarkData(
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
                        bookmarkListWhole.add(
                            WholeScheduleBookmarkData(
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
                binding.recyclerViewBookmark.layoutManager = GridLayoutManager(
                    context,2, GridLayoutManager.VERTICAL, false
                )

                binding.recyclerViewBookmark.setHasFixedSize(true)
                binding.recyclerViewBookmark.adapter = WholeScheduleBookmarkAdapter(bookmarkListWhole)


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
}