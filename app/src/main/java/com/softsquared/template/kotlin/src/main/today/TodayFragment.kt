package com.softsquared.template.kotlin.src.main.today

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentTodayBinding
import com.softsquared.template.kotlin.src.main.today.adapter.MemoAdapter
import com.softsquared.template.kotlin.src.main.today.models.MemoItem

class TodayFragment :
    BaseFragment<FragmentTodayBinding>(FragmentTodayBinding::bind, R.layout.fragment_today) {
    private val memoList:ArrayList<MemoItem> = arrayListOf()
    lateinit var todayMemoAdapter:MemoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미데이터로 리사이클러뷰 테스트
        for(i in 1..5){
            memoList.add(MemoItem(i,"202021",i,"제목","내용",false,"BLUE"))
        }

        // 어댑터
        todayMemoAdapter = MemoAdapter(memoList)
        binding.todayRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todayMemoAdapter
        }



    }
}