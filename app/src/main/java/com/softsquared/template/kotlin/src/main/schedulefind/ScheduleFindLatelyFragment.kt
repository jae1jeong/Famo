package com.softsquared.template.kotlin.src.main.schedulefind

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentMonthlyBinding
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindLatelyBinding
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkData

class ScheduleFindLatelyFragment : BaseFragment<FragmentScheduleFindLatelyBinding>(
    FragmentScheduleFindLatelyBinding::bind, R.layout.fragment_schedule_find_lately) {

    companion object {
        fun newInstance(): ScheduleFindLatelyFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return ScheduleFindLatelyFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createLatelyRecyclerview()
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
}