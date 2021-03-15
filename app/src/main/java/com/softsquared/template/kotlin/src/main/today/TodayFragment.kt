package com.softsquared.template.kotlin.src.main.today

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonElement
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

        showLoadingDialog(context!!)
        TodayService(this).onGetScheduleItems()

        // 어댑터
        val mLayoutManager = LinearLayoutManager(context)
        todayMemoAdapter = MemoAdapter(memoList,context!!,{
            // 디테일 다이얼로그
            val scheduleDetailDialog = ScheduleDetailDialog(context!!)
            // 디테일 다이얼로그 수정하기 버튼
            scheduleDetailDialog.setOnModifyBtnClickedListener {
                // 스케쥴 ID 보내기
                val edit = ApplicationClass.sSharedPreferences.edit()
                edit.putInt(Constants.EDIT_SCHEDULE_ID,it.id)
                edit.apply()

                Constants.IS_EDIT = true

                //바텀 시트 다이얼로그 확장
                (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
            }
            scheduleDetailDialog.start(it)
        },{
            // 일정완료 버튼
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
                        val memoContentJsonElement: JsonElement? = memoJsonObject.get("scheduleMemo")
                        val memoPick = memoJsonObject.get("schedulePick").asInt
                        val memoId = memoJsonObject.get("scheduleID").asInt
                        val memoCreatedAt = memoDate.split(" ")
                        val memoColorInfoJsonElement:JsonElement? = memoJsonObject.get("colorInfo")
                        var memoCreatedAtMonth = ""
                        var memoCreatedAtDay = 0
                        var memoContent = ""
                        if(!memoContentJsonElement!!.isJsonNull) {
                            memoContent = memoContentJsonElement.asString
                        }

                        var memoColorInfo:String? = null
                        if(!memoColorInfoJsonElement!!.isJsonNull){
                            memoColorInfo = memoColorInfoJsonElement.asString
                        }

                        // 날짜 동그란 뷰에 들어갈 날짜와 월 스트링 분리
                        for (i in 0..1) {
                            if (i > 0) {
                                memoCreatedAtMonth = memoCreatedAt[i].replace(" ","")
                            } else {
                                memoCreatedAtDay = memoCreatedAt[i].replace(" ","").toInt()
                            }
                        }

                        // 메모 체크되어있는지
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
                                memoColorInfo
                            )
                        )
                    }
                    todayMemoAdapter?.setNewMemoList(memoList)
                    // 메모가 없으면 뷰를 보여줘야하기 때문에 체크 메서드 함수 호출
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