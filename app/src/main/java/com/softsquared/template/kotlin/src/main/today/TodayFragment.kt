package com.softsquared.template.kotlin.src.main.today

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentTodayBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.today.adapter.MemoAdapter
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse
import com.softsquared.template.kotlin.util.ScheduleDetailDialog

class TodayFragment :
    BaseFragment<FragmentTodayBinding>(FragmentTodayBinding::bind, R.layout.fragment_today)
    ,TodayView
{
    val memoList:ArrayList<MemoItem> = arrayListOf()
    lateinit var todayMemoAdapter:MemoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//         더미데이터로 리사이클러뷰 테스트
//        for(i in 1..5){
//            memoList.add(MemoItem(i,"202021",i,"오늘","내용1",false,"BLUE"))
//            memoList.add(MemoItem(i,"202021",i,"테스트2","내용2에요",true,"BLUE"))
//
//        }

        showLoadingDialog(context!!)
        TodayService(this).onGetScheduleItems()

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

        // 리사이클러뷰 아이템 스와이프,드래그
        val swipe = object: MemoSwipeHelper(todayMemoAdapter,context!!,binding.todayRecyclerView,200)
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
                    object: SwipeButtonClickListener {
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
                    object: SwipeButtonClickListener {
                        override fun onClick(pos: Int) {
                            showCustomToast("share $pos")
                        }
                    }
                ))
            }

        }
    }

    fun checkIsMemoListEmpty(){
        // 메모가 없을 경우 메모가 없는 뷰 나타나게 하기, 있으면 GONE 처리
        if(todayMemoAdapter.itemCount > 0){
            binding.todayFrameLayoutNoItem.visibility = View.GONE
        }else{
            binding.todayFrameLayoutNoItem.visibility = View.VISIBLE
        }
    }

    override fun onGetScheduleItemsSuccess(response: ScheduleItemsResponse) {
        if(response.isSuccess){
            when(response.code){
                100->{

                    val memoJsonArray = response.data.asJsonArray
                    for (i in 0 until memoJsonArray.size()){
                        val memoJsonObject = memoJsonArray[i].asJsonObject
                        val memoDate = memoJsonObject.get("scheduleDate").asString
                        val memoTitle = memoJsonObject.get("scheduleName").asString
                        var memoContent :String? = memoJsonObject.get("scheduleMemo").toString()
                        val memoIsChecked = memoJsonObject.get("schedulePick").asBoolean
                        val memoId = memoJsonObject.get("scheduleID").asInt
                        val memoCreatedAt = memoDate.split(" ")
                        var memoCreatedAtMonth = ""
                        var memoCreatedAtDay = 0
                        for(i in 0 until memoCreatedAt.size){
                            if(i > 0){
                                memoCreatedAtMonth = memoCreatedAt[i]
                            }else{
                                memoCreatedAtDay = memoCreatedAt[i].toInt()
                            }
                        }
                        if(memoContent == null){
                            memoContent = ""
                        }
                        memoList.add(MemoItem(memoId,memoCreatedAtMonth,memoCreatedAtDay,memoTitle,memoContent,memoIsChecked,"BLUE"))
                    }
                    todayMemoAdapter.setNewMemoList(memoList)
                    checkIsMemoListEmpty()
                    dismissLoadingDialog()
                }
                else->{
                    dismissLoadingDialog()
                    showCustomToast(response.message.toString())
                }
            }
        }else{
            dismissLoadingDialog()
            showCustomToast(response.message.toString())
        }
    }

    override fun onGetScheduleItemsFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }

}