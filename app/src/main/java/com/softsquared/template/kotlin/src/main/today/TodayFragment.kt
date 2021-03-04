package com.softsquared.template.kotlin.src.main.today

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentTodayBinding
import com.softsquared.template.kotlin.src.main.today.adapter.MemoAdapter
import com.softsquared.template.kotlin.src.main.today.adapter.MemoSwipeHelper
import com.softsquared.template.kotlin.src.main.today.adapter.SwipeButton
import com.softsquared.template.kotlin.src.main.today.adapter.SwipeButtonClickListener
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
            memoList.add(MemoItem(i,"202021",i,"제목","내용",true,"BLUE"))

        }

        // 어댑터
        val mLayoutManager = LinearLayoutManager(context)
        todayMemoAdapter = MemoAdapter(memoList,context!!)
        binding.todayRecyclerView.apply {
            layoutManager = mLayoutManager
            adapter = todayMemoAdapter
        }

        // 리사이클러뷰 아이템 스와이프
        val swipe = object:MemoSwipeHelper(context!!,binding.todayRecyclerView,200)
        {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<SwipeButton>
            ) {
                // add button
                buffer.add(SwipeButton(context!!,
                    "Delete",
                    30,
                    R.drawable.schedule_delete,
                    Color.parseColor("#32363C"),
                    object:SwipeButtonClickListener{
                        override fun onClick(pos: Int) {
                            showCustomToast("delete $pos")
                        }
                    }
                    ))
                buffer.add(SwipeButton(context!!,
                    "Share",
                    30,
                    R.drawable.schedule_share,
                    Color.parseColor("#FFAE2A"),
                    object:SwipeButtonClickListener{
                        override fun onClick(pos: Int) {
                            showCustomToast("share $pos")
                        }
                    }
                ))
            }

        }
    }

}