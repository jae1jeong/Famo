package com.softsquared.template.kotlin.src.main.schedulefind

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindBinding
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.SchedulePartAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleWholeAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.SchedulePartData
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleWholeData

class ScheduleFindFragment : BaseFragment<FragmentScheduleFindBinding>
    (FragmentScheduleFindBinding::bind, R.layout.fragment_schedule_find) {

    private val partList:ArrayList<ScheduleWholeData> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 즐겨찾기/최근 일정 리사이클러뷰
        createBookmarkRecyclerview()

        // 전체일정 리사이클러뷰
        createWholeScheduleRecyclerview()

        //테스트 데이터
        val partList = arrayListOf(
                SchedulePartData("제목","시간")
        )

        //즐겨찾기/최근 일정 리사이클러뷰 연결
        binding.recyclerViewPart.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewPart.setHasFixedSize(true)
        binding.recyclerViewPart.adapter = SchedulePartAdapter(partList)

    }

    private fun createWholeScheduleRecyclerview() {
        //테스트 데이터
        val wholeList = arrayListOf(
                ScheduleWholeData("2021.02.10", "제목", "내용",
                        R.drawable.schedule_find_bookmark),
                ScheduleWholeData("2021.02.10", "제목2", "내용2",
                        R.drawable.schedule_find_bookmark),
                ScheduleWholeData("2021.02.10", "제목3", "내용3",
                        R.drawable.schedule_find_bookmark),
                ScheduleWholeData("2021.02.10", "제목4", "내용4",
                        R.drawable.schedule_find_bookmark)
        )

        //전체일정 리사이큘러뷰 연결
        binding.recyclerviewWhole.layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL,
                        false)
        binding.recyclerviewWhole.setHasFixedSize(true)
        binding.recyclerviewWhole.adapter = ScheduleWholeAdapter(wholeList)
    }

    private fun createBookmarkRecyclerview() {
        //테스트 데이터
        val partList = arrayListOf(
                SchedulePartData("제목", "시간"),
                SchedulePartData("제목", "시간")
        )

        // 즐겨찾기/최근 일정 리사이클러뷰 연결
        binding.recyclerViewPart.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewPart.setHasFixedSize(true)
        binding.recyclerViewPart.adapter = SchedulePartAdapter(partList)
    }
}