package com.makeus.jfive.famo.src.main.today

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonElement
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseFragment
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.databinding.FragmentTodayBinding
import com.makeus.jfive.famo.src.main.MainActivity
import com.makeus.jfive.famo.src.main.ResultBottomSheetDialog
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse
import com.makeus.jfive.famo.src.main.today.adapter.MemoAdapter
import com.makeus.jfive.famo.src.main.today.models.*
import com.makeus.jfive.famo.src.mypage.MyPageService
import com.makeus.jfive.famo.src.mypage.MyPageView
import com.makeus.jfive.famo.src.mypage.models.MonthsAchievementsResponse
import com.makeus.jfive.famo.src.mypage.models.MyPageResponse
import com.makeus.jfive.famo.src.mypage.models.RestScheduleCountResponse
import com.makeus.jfive.famo.src.mypage.models.TotalScheduleCountResponse
import com.makeus.jfive.famo.util.AskDialog
import com.makeus.jfive.famo.util.Constants
import com.makeus.jfive.famo.util.ScheduleDetailDialog
import kotlinx.android.synthetic.main.fragment_today.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TodayFragment() :
    BaseFragment<FragmentTodayBinding>(FragmentTodayBinding::bind, R.layout.fragment_today)
    ,TodayView, MyPageView {

    private var initialMemoListCount = 0

    companion object{
        val memoList:ArrayList<MemoItem> = arrayListOf()
        var todayMemoAdapter:MemoAdapter ?= null
        var doneScheduleCount = 0
        var restScheduleCount = 0
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 애니메이션 시작
        today_shimmer_header_frame_layout.startShimmerAnimation()
        today_shimmer_frame_layout.startShimmerAnimation()

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

            scheduleDetailDialog.start(it,it.formDateStr)
        }, {
            changeCount(it.isChecked,"CHECK")
            // 일정완료 버튼
            TodayService(this).onPostCheckItem(CheckItemRequest(it.id))
        },{

        })
        binding.todayRecyclerView.apply {
            layoutManager = mLayoutManager
            adapter = todayMemoAdapter
        }

        // 메모가 없을때 뷰 클릭
        binding.todayImageNoItem.setOnClickListener {
            (activity as MainActivity).stateChangeBottomSheet(Constants.COLLASPE)
        }

        fun changeSchedulePosition(fromPos:Int,targetPos:Int){
            Log.d("순서", "$fromPos 에서 -> $targetPos 로")
            Log.d("순서", "isSuccess: ${memoList[fromPos].id}번 일정 위치를 ${memoList[fromPos].title} $targetPos 로 이동 ${memoList.size}")
            TodayService(this).onPostChangeItemPosition(ChangePositionItemRequest(memoList[fromPos].id,targetPos))
        }
        // 리사이클러뷰 아이템 스와이프,드래그
        val swipe = object: MemoSwipeHelper(todayMemoAdapter!!, context!!, binding.todayRecyclerView, 200, { fromPos, targetPos ->
            // 서로 바뀐 것 같아서 반대로
            changeSchedulePosition(targetPos,fromPos)
            for (i in 0 until memoList.size) Log.d("순서", "index:$i ${memoList[i].title}")
        })
        {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<SwipeButton>
            ) {

                // R.drawable.schedule_delete
                // add button
                buffer.add(SwipeButton(context!!,
                    "삭제",
                    50,
                    0,
                    Color.parseColor("#32363C"),
                    object: SwipeButtonClickListener {
                        override fun onClick(pos: Int) {
                            AskDialog(context!!)
                                .setTitle("일정삭제")
                                .setMessage("일정을 삭제하시겠습니까?")
                                .setPositiveButton("삭제"){
                                    showLoadingDialog(context!!)
                                    if(memoList[pos].isChecked){
                                        changeCount(false,Constants.DELETE_CHECKED_ITEM)
                                    }else{
                                        changeCount(false,Constants.DELETE_NOT_CHECKED_ITEM)
                                    }
                                    TodayService(this@TodayFragment).onPutDeleteMemo(memoList[pos].id)
                                }
                                .setNegativeButton("취소"){
                                }.show()
                        }
                    }
                    ))
                //R.drawable.schedule_share
                buffer.add(SwipeButton(context!!,
                    "공유",
                    50,
                    0,
                    Color.parseColor("#FFAE2A"),
                    object: SwipeButtonClickListener {
                        override fun onClick(pos: Int) {
                            val sendStringData ="${memoList[pos].title}\n${memoList[pos].description}\n${memoList[pos].formDateStr}"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT,sendStringData)
                                type = "text/plain"
                            }
                            if(sendIntent.resolveActivity((activity as MainActivity).packageManager)!=null){
                                startActivity(sendIntent)
                            }
                        }
                    }
                ))
            }
        }
    }

    fun showResultFragment(){
        val resultFragment = ResultBottomSheetDialog()
        resultFragment.show(childFragmentManager,resultFragment.tag)
    }

    override fun viewPagerApiRequest() {
        super.viewPagerApiRequest()

        GlobalScope.launch(Dispatchers.Main) {
            val loadingAnimation = launch {
                delay(1000)
                today_shimmer_frame_layout.stopShimmerAnimation()
                today_shimmer_header_frame_layout.stopShimmerAnimation()
                today_shimmer_header_frame_layout.visibility = View.GONE
                today_shimmer_frame_layout.visibility = View.GONE
                binding.todayRecyclerView.visibility = View.VISIBLE
                binding.todayLayoutTopMent.visibility = View.VISIBLE
                checkIsMemoListEmpty()
            }
        }
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
                        val memoStatus = memoJsonObject.get("scheduleStatus").asInt
                        val memoId = memoJsonObject.get("scheduleID").asInt
                        val memoCreatedAt = memoDate.split(" ")
                        val memoColorInfoJsonElement:JsonElement? = memoJsonObject.get("colorInfo")
                        var memoCreatedAtMonth = ""
                        var memoCreatedAtDay = 0
                        var memoContent = ""
                        if(!memoContentJsonElement!!.isJsonNull) {
                            memoContent = memoContentJsonElement.asString
                        }
                        val memoScheduleOrder = memoJsonObject.get("scheduleOrder").asInt
                        val memoScheduleFormDate = memoJsonObject.get("scheduleFormDate").asString

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
                        memoIsChecked = memoStatus == 1

                        memoList.add(
                            MemoItem(
                                memoId,
                                memoCreatedAtMonth,
                                memoCreatedAtDay,
                                memoTitle,
                                memoContent,
                                memoIsChecked,
                                memoColorInfo,
                                memoScheduleFormDate,
                                    memoScheduleOrder
                            )
                        )
                    }
                    todayMemoAdapter?.setNewMemoList(memoList)
                    initialMemoListCount=  memoList.size
                }
                else->{
                }
            }
        }else{
        }
    }

    override fun onGetScheduleItemsFailure(message: String) {

    }

    override fun onDeleteMemoSuccess(response: BaseResponse,scheduleID:Int) {
        if(response.isSuccess){
            when(response.code){
                100->{
                    memoList.removeIf {
                        it.id == scheduleID
                    }
                    todayMemoAdapter?.setNewMemoList(memoList)
                    checkIsMemoListEmpty()
                    dismissLoadingDialog()
                    showCustomToast("일정이 성공적으로 삭제 되었습니다. :)")
                }else->{
                dismissLoadingDialog()
            }
            }
        }else{
            dismissLoadingDialog()
        }
    }

    override fun onDeleteMemoFailure(message: String) {
        dismissLoadingDialog()
    }

    override fun onPostItemCheckSuccess(response: BaseResponse) {
        if(response.isSuccess && response.code == 100){
            Log.d("todayFragment", "onPostItemCheckSuccess: 일정 체크 성공")

            // 오늘 일정을 모두 완료할 경우
//            if(initialMemoListCount != 0 && restScheduleCount == 0){
//                showResultFragment()
//            }
        }else{
        }

    }

    override fun onPostItemCheckFailure(message: String) {
    }

    override fun onGetUserTopCommentSuccess(response: TopCommentResponse) {
        if(response.isSuccess && response.code == 100){
            Log.d("todayFragment", "onGetUserTopCommentSuccess: 상단멘트 조회 성공")
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
        }
    }

    override fun onGetUserTopCommentFailure(message: String) {
    }

    override fun onPostSchedulePositionSuccess(response: BaseResponse) {
        if(response.isSuccess && response.code == 100){
            Log.d("MainActivity", "onPostSchedulePositionSuccess: ${response.message}")
        }else{
        }
    }

    override fun onPostSchedulePositionFailure(message: String) {
    }

    override fun onGetDetailMemoSuccess(response: DetailMemoResponse) {
    }

    override fun onGetDetailMemoFailure(message: String) {
    }

    override fun onGetMyPageSuccess(response: MyPageResponse) {

    }

    override fun onGetMyPageFail(message: String) {
    }

    override fun onGetRestScheduleCountSuccess(response: RestScheduleCountResponse) {
        if(response.isSuccess && response.code == 100){
            val responseJsonArray = response.data.asJsonArray
            responseJsonArray.forEach {
                val jsonObject = it.asJsonObject
                restScheduleCount = jsonObject.get("remainScheduleCount").asInt
            }
            Log.d("TAG", "onGetRestScheduleCountSuccess: $restScheduleCount")
            binding.todayTextRestSchedule.text=  "남은 일정 ${restScheduleCount}개"
        }else{
            Log.d("TodayFragment", "onGetRestScheduleCountSuccess: ${response.message}")
        }
    }

    override fun onGetRestScheduleCountFailure(message: String) {
    }

    override fun onGetTotalScheduleCountSuccess(response: TotalScheduleCountResponse) {
        if(response.isSuccess && response.code == 100){
            Log.d("todayFragment", "onGetUserTopCommentSuccess: 해낸 일정 조회 성공")
            doneScheduleCount = response.totaldonedata[0].doneScheduleCount.toString().toInt()
            binding.todayTextDoneSchedule.text = "해낸 일정 ${doneScheduleCount}개"
        }else{
        }

    }

    override fun onGetTotalScheduleCountFailure(message: String) {
        Log.d("TodayFragment", "onGetTotalScheduleCountFailure:$message ")
    }

    override fun onGetMonthsAchievmentsSuccess(response: MonthsAchievementsResponse) {
    }

    override fun onGetMonthsAchievmentsFailure(message: String) {
    }


    fun changeCount(isChecked:Boolean,state:String){
        when(state){
            Constants.CHECK->{
                if(!isChecked){
                    restScheduleCount--
                    doneScheduleCount++
                }else{
                    restScheduleCount++
                    doneScheduleCount--
                }
            }
            Constants.DELETE_CHECKED_ITEM->{
                doneScheduleCount--
            }
            Constants.DELETE_NOT_CHECKED_ITEM->{
                restScheduleCount--
            }
            else->{
                showCustomToast("잘못된 요청입니다.")
            }
        }
        binding.todayTextRestSchedule.text = "남은일정 ${restScheduleCount}개"
        binding.todayTextDoneSchedule.text = "해낸일정 ${doneScheduleCount}개"

    }
}