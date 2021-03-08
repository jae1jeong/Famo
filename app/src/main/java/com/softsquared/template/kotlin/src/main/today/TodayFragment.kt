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
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.addmemo.AddMemoFragment
import com.softsquared.template.kotlin.src.main.today.adapter.MemoAdapter
import com.softsquared.template.kotlin.src.main.today.adapter.MemoSwipeHelper
import com.softsquared.template.kotlin.src.main.today.adapter.SwipeButton
import com.softsquared.template.kotlin.src.main.today.adapter.SwipeButtonClickListener
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.util.ScheduleDetailDialog

class TodayFragment :
    BaseFragment<FragmentTodayBinding>(FragmentTodayBinding::bind, R.layout.fragment_today) {
    private val memoList:ArrayList<MemoItem> = arrayListOf()
    lateinit var todayMemoAdapter:MemoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미데이터로 리사이클러뷰 테스트
        for(i in 1..5){
            memoList.add(MemoItem(i,"202021",i,"오늘","내용1",false,"BLUE"))
            memoList.add(MemoItem(i,"202021",i,"테스트2","내용2에요",true,"BLUE"))

        }

        // 어댑터
        val mLayoutManager = LinearLayoutManager(context)
        todayMemoAdapter = MemoAdapter(memoList,context!!){
            val scheduleDetailDialog = ScheduleDetailDialog(context!!)
            scheduleDetailDialog.setOnModifyBtnClickedListener {
                (activity as MainActivity).showBottomAddScheduleSheetDialog()
            }
            scheduleDetailDialog.start(it)
        }
        binding.todayRecyclerView.apply {
            layoutManager = mLayoutManager
            adapter = todayMemoAdapter
        }

        binding.todayImageNoItem.setOnClickListener {
            (activity as MainActivity).showBottomAddScheduleSheetDialog()

        }
        // 메모가 없을 경우 메모가 없는 뷰 나타나게 하기, 있으면 GONE 처리
        if(todayMemoAdapter.itemCount > 0){
            binding.todayFrameLayoutNoItem.visibility = View.GONE
        }else{
            binding.todayFrameLayoutNoItem.visibility = View.VISIBLE
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