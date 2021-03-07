package com.softsquared.template.kotlin.src.main.schedulefind

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindBookmarkBinding
import com.softsquared.template.kotlin.databinding.FragmentScheduleFindLatelyBinding
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleBookmarkData

class ScheduleFindBookmarkFragment : BaseFragment<FragmentScheduleFindBookmarkBinding>(
    FragmentScheduleFindBookmarkBinding::bind, R.layout.fragment_schedule_find_bookmark) {

    companion object {
        fun newInstance(): ScheduleFindBookmarkFragment {    // shs: 함수의 반환 형이 Fragment 형이라...
            return ScheduleFindBookmarkFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createBookmarkRecyclerview()
    }

    private fun createBookmarkRecyclerview() {
        //테스트 데이터
        val bookmarkList = arrayListOf(
            ScheduleBookmarkData("제목", "시간"),
            ScheduleBookmarkData("제목", "시간")
        )

        // 즐겨찾기/최근 일정 리사이클러뷰 연결
        binding.recyclerViewBookmark.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewBookmark.setHasFixedSize(true)
        binding.recyclerViewBookmark.adapter = ScheduleBookmarkAdapter(bookmarkList)
    }
}