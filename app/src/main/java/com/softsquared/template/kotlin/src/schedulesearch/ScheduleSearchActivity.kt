package com.softsquared.template.kotlin.src.schedulesearch

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityScheduleSearchBinding
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleBookmarkAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.adapter.ScheduleWholeAdapter
import com.softsquared.template.kotlin.src.main.schedulefind.models.ScheduleWholeData
import com.softsquared.template.kotlin.src.schedulesearch.adapter.ScheduleSearchListAdapter
import com.softsquared.template.kotlin.src.schedulesearch.models.SearchListData

class ScheduleSearchActivity : BaseActivity<ActivityScheduleSearchBinding>
    (ActivityScheduleSearchBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //임시 검색기록 리사이클러뷰
        createSearchList()

        //뒤로가기
        binding.searchBack.setOnClickListener {

        }

    }

    private fun createSearchList() {
        val searchList = arrayListOf(
            SearchListData("검색1"),
            SearchListData("검색2"),
            SearchListData("검색3"),
            SearchListData("검색4")
        )

        //전체일정 리사이큘러뷰 연결
        binding.recyclerviewSearchList.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerviewSearchList.setHasFixedSize(true)
        binding.recyclerviewSearchList.adapter = ScheduleSearchListAdapter(searchList)
    }

}