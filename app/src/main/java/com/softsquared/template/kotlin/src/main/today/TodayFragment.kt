package com.softsquared.template.kotlin.src.main.today

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
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
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.mypage.models.DoneScheduleCountResponse
import com.softsquared.template.kotlin.src.main.today.adapter.MemoAdapter
import com.softsquared.template.kotlin.src.main.today.models.*
import com.softsquared.template.kotlin.src.mypage.MyPageService
import com.softsquared.template.kotlin.src.mypage.MyPageView
import com.softsquared.template.kotlin.src.mypage.models.MyPageResponse
import com.softsquared.template.kotlin.src.mypage.models.RestScheduleCountResponse
import com.softsquared.template.kotlin.util.Constants
import com.softsquared.template.kotlin.util.ScheduleDetailDialog

class TodayFragment() :
    BaseFragment<FragmentTodayBinding>(FragmentTodayBinding::bind, R.layout.fragment_today)
    ,TodayView,MyPageView{


    companion object{
        val memoList:ArrayList<MemoItem> = arrayListOf()
        var todayMemoAdapter:MemoAdapter ?= null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 어댑터
        val mLayoutManager = LinearLayoutManager(context)
        todayMemoAdapter = MemoAdapter(memoList, context!!, {
            // 디테일 다이얼로그
            val scheduleDetailDialog = ScheduleDetailDialog(context!!)
            // 디테일 다이얼로그 수정하기 버튼
            scheduleDetailDialog.setOnModifyBtnClickedListener {
                // 스케쥴 ID 보내기
                val edit = ApplicationClass.sSharedPreferences.edit()
                edit.putInt(Constants.EDIT_SCHEDULE_ID, it.id)
                edit.apply()
                Constants.IS_EDIT = true

                //바텀 시트 다이얼로그 확장
                (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
            }
            scheduleDetailDialog.start(it)
        }, {
            // 일정완료 버튼
            TodayService(this).onPostCheckItem(CheckItemRequest(it.id))
        },{

        })
        binding.todayRecyclerView.apply {
            layoutManager = mLayoutManager
            adapter = todayMemoAdapter
        }

        // 메모가 없을때
        binding.todayImageNoItem.setOnClickListener {
            (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
        }

        fun changeSchedulePosition(targetPos:Int,fromPos:Int){
            TodayService(this).onPostChangeItemPosition(ChangePositionItemRequest(memoList[fromPos].id,targetPos))
        }
        // 리사이클러뷰 아이템 스와이프,드래그
        val swipe = object: MemoSwipeHelper(todayMemoAdapter!!,context!!,binding.todayRecyclerView,200,{
          pos1,pos2->   changeSchedulePosition(pos1,pos2)
        })
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

    override fun viewPagerApiRequest() {
        super.viewPagerApiRequest()
        showLoadingDialog(context!!)
        // 오늘 일정 조회
        TodayService(this).onGetScheduleItems()
        // 상단멘트
        TodayService(this).onGetTopComment()
        MyPageService(this).tryGetTotalScheduleCount()
        MyPageService(this).tryGetRestScheduleCount("today")

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


    override fun onResume() {
        super.onResume()
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
                        showCustomToast(scheduleID.toString())
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
            showCustomToast(response.message.toString())
        }

    }

    override fun onPostItemCheckFailure(message: String) {
        showCustomToast(message)
    }

    override fun onGetUserTopCommentSuccess(response: TopCommentResponse) {
        if(response.isSuccess && response.code == 100){
            when(response.goalStatus){
                // 디데이 설정 o
                1 -> {
                    val goalTitle = response.goalTitle
                    val goalDday = response.Dday
                    binding.todayTextGoalTitle.text = "${goalTitle}까지"
                    binding.todayTextGoalSubTitle.text = "D-${goalDday}일 남았어요!"
                }
                // 디데이 설정 x
                -1 ->{
                    val nickName = response.nickname
                    val titleComment = response.titleComment
                    binding.todayTextGoalTitle.text = "${nickName}님"
                    binding.todayTextGoalSubTitle.text = titleComment

                }
            }
        }else{
            showCustomToast(response.message.toString())
        }
    }

    override fun onGetUserTopCommentFailure(message: String) {
    }

    override fun onPostSchedulePositionSuccess(response: BaseResponse) {
        if(response.isSuccess && response.code == 100){
            Log.d("MainActivity", "onPostSchedulePositionSuccess: ${response.message}")
        }else{
            showCustomToast(response.message.toString())
        }
        dismissLoadingDialog()
    }

    override fun onPostSchedulePositionFailure(message: String) {
        showCustomToast(message)
    }

    override fun onGetMyPageSuccess(response: MyPageResponse) {

    }

    override fun onGetMyPageFail(message: String) {
    }

    override fun onGetRestScheduleCountSuccess(response: RestScheduleCountResponse) {
        showCustomToast(response.message.toString())
        if(response.isSuccess && response.code == 100){
            binding.todayTextRestSchedule.text=  "남은일정 ${response.remainScheduleCount}개"
        }else{
            Log.d("MyPageFragment", "onGetRestScheduleCountSuccess: ${response.message}")
        }
    }

    override fun onGetRestScheduleCountFailure(message: String) {
    }

    override fun onGetDoneScheduleCountSuccess(response: DoneScheduleCountResponse) {
        if (response.isSuccess && response.code == 100){
            val totalDataJsonArray = response.totaldata.asJsonArray
            totalDataJsonArray.forEach {
                val totalData = it.asJsonObject.get("totalScheduleCount").asString
            }
            val DoneScheduleDataJsonArray = response.totaldonedata.asJsonArray
            DoneScheduleDataJsonArray.forEach {
                val doneData = it.asJsonObject.get("doneScheduleCount").asString
                binding.todayTextDoneSchedule.text = "해낸일정 ${doneData}개"
            }
        }else{
            Log.d("MyPageFragment", "onGetRestScheduleCountSuccess: ${response.message}")
        }
    }

    override fun onGetDoneScheduleCountFailure(message: String) {
    }

}