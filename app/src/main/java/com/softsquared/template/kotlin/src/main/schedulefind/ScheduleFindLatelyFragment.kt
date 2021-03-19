package com.softsquared.template.kotlin.src.main.schedulefind

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentMonthlyBinding
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindLatelyBinding
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkData
import com.softsquared.template.kotlin.src.wholeschedule.WholeScheduleService
import com.softsquared.template.kotlin.src.wholeschedule.WholeScheduleView
import com.softsquared.template.kotlin.src.wholeschedule.models.LatelyScheduleInquiryResponse

class ScheduleFindLatelyFragment : BaseFragment<FragmentScheduleFindLatelyBinding>(
    FragmentScheduleFindLatelyBinding::bind, R.layout.fragment_schedule_find_lately),
    WholeScheduleView{

    companion object {
        fun newInstance(): ScheduleFindLatelyFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return ScheduleFindLatelyFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        WholeScheduleService(this).tryGetLatelyScheduleInquiry(10,10)

//        createLatelyRecyclerview()
    }

    private fun createLatelyRecyclerview() {
        //테스트 데이터
        val latelyList = arrayListOf(
            ScheduleBookmarkData("최근제목", "최근시간"),
            ScheduleBookmarkData("최근제목", "최근시간")
        )

        // 즐겨찾기/최근 일정 리사이클러뷰 연결
        binding.recyclerViewLately.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewLately.setHasFixedSize(true)
        binding.recyclerViewLately.adapter = ScheduleBookmarkAdapter(latelyList)
    }

    override fun onGetLatelyScheduleInquirySuccess(response: LatelyScheduleInquiryResponse) {

        when(response.code){
            100 -> {
                showCustomToast("즐겨찾기 성공")
                Log.d("TAG", "onGetLatelyScheduleInquirySuccess: 최근일정조회성공")

                val latelyList = arrayListOf(
                    ScheduleBookmarkData("최근제목", "최근시간"),
                    ScheduleBookmarkData("최근제목", "최근시간")
                )

                // 즐겨찾기/최근 일정 리사이클러뷰 연결
                binding.recyclerViewLately.layoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.VERTICAL, false
                )
                binding.recyclerViewLately.setHasFixedSize(true)
                binding.recyclerViewLately.adapter = ScheduleBookmarkAdapter(latelyList)

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