package com.softsquared.template.kotlin.src.main.today

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.FragmentTodayBinding
import com.softsquared.template.kotlin.src.main.AddMemoService
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.today.adapter.MemoAdapter
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse
import com.softsquared.template.kotlin.util.Constants
import com.softsquared.template.kotlin.util.ScheduleDetailDialog

class TodayFragment :
    BaseFragment<FragmentTodayBinding>(FragmentTodayBinding::bind, R.layout.fragment_today)
    ,TodayView
{

    companion object{
        val memoList:ArrayList<MemoItem> = arrayListOf()
        var todayMemoAdapter:MemoAdapter ?= null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//         더미데이터로 리사이클러뷰 테스트
//        for(i in 1..5){
//            memoList.add(MemoItem(i,"202021",i,"오늘","내용1",false,"BLUE"))
//            memoList.add(MemoItem(i,"202021",i,"테스트2","내용2에요",true,"BLUE"))
//
//        }

//        showLoadingDialog(context!!)
//        TodayService(this).onGetScheduleItems()

        // 어댑터
        val mLayoutManager = LinearLayoutManager(context)
        todayMemoAdapter = MemoAdapter(memoList,context!!,{
            val scheduleDetailDialog = ScheduleDetailDialog(context!!)
            scheduleDetailDialog.setOnModifyBtnClickedListener {
                val edit = ApplicationClass.sSharedPreferences.edit()
                edit.putInt(Constants.EDIT_SCHEDULE_ID,it.id)
                edit.apply()
                Constants.IS_EDIT = true
                (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
            }
            scheduleDetailDialog.start(it)
        },{
            showLoadingDialog(context!!)
            TodayService(this).onPostCheckItem(it.id)
        })
        binding.todayRecyclerView.apply {
            layoutManager = mLayoutManager
            adapter = todayMemoAdapter
        }


        // 메모가 없을때
        binding.todayImageNoItem.setOnClickListener {
            (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
        }

        // 리사이클러뷰 아이템 스와이프,드래그
        val swipe = object: MemoSwipeHelper(todayMemoAdapter!!,context!!,binding.todayRecyclerView,200)
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
                            showLoadingDialog(context!!)
                            TodayService(this@TodayFragment).onPutDeleteMemo(memoList[pos].id)
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
                        }
                    }
                ))
            }
        }
    }

    fun reloadItems(context:Context){
        showLoadingDialog(context)
        TodayService(this).onGetScheduleItems()
    }

    fun checkIsMemoListEmpty(){
        // 메모가 없을 경우 메모가 없는 뷰 나타나게 하기, 있으면 GONE 처리
        todayMemoAdapter?.let{
            if(todayMemoAdapter!!.itemCount > 0){
                binding.todayFrameLayoutNoItem.visibility = View.GONE
            }else{
                binding.todayFrameLayoutNoItem.visibility = View.VISIBLE
            }
        }
    }

    override fun onGetScheduleItemsSuccess(response: ScheduleItemsResponse) {
        if(response.isSuccess){
            when(response.code){
                100 -> {
                    memoList.clear()
                    val memoJsonArray = response.data.asJsonArray
                    for (i in 0 until memoJsonArray.size()) {
                        val memoJsonObject = memoJsonArray[i].asJsonObject
                        val memoDate = memoJsonObject.get("scheduleDate").asString
                        val memoTitle = memoJsonObject.get("scheduleName").asString
                        var memoContent: String? = memoJsonObject.get("scheduleMemo").toString()
                        val memoPick = memoJsonObject.get("schedulePick").asInt
                        val memoId = memoJsonObject.get("scheduleID").asInt
                        val memoCreatedAt = memoDate.split(" ")
                        var memoCreatedAtMonth = ""
                        var memoCreatedAtDay = 0
                        Log.d("tag", "onGetScheduleItemsSuccess:[$memoDate] $memoCreatedAt")
                        for (i in 0..1) {
                            if (i > 0) {
                                memoCreatedAtMonth = memoCreatedAt[i].replace(" ","")
                            } else {
                                memoCreatedAtDay = memoCreatedAt[i].replace(" ","").toInt()
                            }
                        }
                        if (memoContent == null) {
                            memoContent = ""
                        }
                        var memoIsChecked :Boolean? = null
                        memoIsChecked = memoPick >= 0
                        memoList.add(
                            MemoItem(
                                memoId,
                                memoCreatedAtMonth,
                                memoCreatedAtDay,
                                memoTitle,
                                memoContent,
                                memoIsChecked,
                                "BLUE"
                            )
                        )
                    }
                    todayMemoAdapter?.setNewMemoList(memoList)
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

    override fun onDeleteMemoSuccess(response: BaseResponse,scheduleID:Int) {
        if(response.isSuccess){
            when(response.code){
                100->{
                    showCustomToast(response.message.toString())
                    memoList.removeIf {
                        it.id == scheduleID
                    }
                    todayMemoAdapter?.setNewMemoList(memoList)
                    checkIsMemoListEmpty()
                    dismissLoadingDialog()
                }else->{
                dismissLoadingDialog()
                showCustomToast(response.message.toString())
            }
            }
        }else{
            dismissLoadingDialog()
            showCustomToast(response.message.toString())
        }
    }

    override fun onDeleteMemoFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }

    override fun onPostItemCheckSuccess(response: BaseResponse) {
        if(response.isSuccess && response.code == 100){
            Log.d("todayFragment", "onPostItemCheckSuccess: 일정 체크 성공")
        }else{
            dismissLoadingDialog()
            showCustomToast(response.message.toString())
        }

    }

    override fun onPostItemCheckFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }

}