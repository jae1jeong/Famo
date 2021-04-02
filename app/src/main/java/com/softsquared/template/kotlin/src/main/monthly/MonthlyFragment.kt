package com.softsquared.template.kotlin.src.main.monthly

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonElement
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.ApplicationClass
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.FragmentMonthlyBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.models.DetailMemoResponse
import com.softsquared.template.kotlin.src.main.monthly.adapter.MonthlyMemoAdapter
import com.softsquared.template.kotlin.src.main.monthly.models.AllMemoResponse
import com.softsquared.template.kotlin.src.main.monthly.models.MonthlyMemoItemResponse
import com.softsquared.template.kotlin.src.main.monthly.models.MonthlyUserDateListResponse
import com.softsquared.template.kotlin.src.main.today.TodayService
import com.softsquared.template.kotlin.src.main.today.TodayView
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse
import com.softsquared.template.kotlin.src.main.today.models.TopCommentResponse
import com.softsquared.template.kotlin.util.*
import kotlinx.android.synthetic.main.fragment_monthly.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList

class MonthlyFragment : BaseFragment<FragmentMonthlyBinding>(FragmentMonthlyBinding::bind, R.layout.fragment_monthly),MonthlyView,TodayView{
    val memoList:ArrayList<MemoItem> = arrayListOf()
    private val userCheckedDateList:ArrayList<LocalDate> = arrayListOf()
    private var todayDate:LocalDate ?= null
    private var selectedDate:LocalDate ?= null
    private val userMemos:ArrayList<String> = arrayListOf()

    private var selectedView:TextView ?= null
    private var targetMonth:Int? = null
    private var targetYear:Int? = null

    val daysOfWeek = arrayOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )

    companion object{
        var monthlyMemoAdapter:MonthlyMemoAdapter?=null
    }



    private var currentMonth:YearMonth = YearMonth.now()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if(bundle != null){
            val strDate = bundle.getString("selectedDate")
            if(strDate != ""){
                Log.d("TAG", "onViewCreated: $strDate")
                val dateStrList = strDate?.split("-")
                currentMonth = YearMonth.parse("${dateStrList?.get(0)!!}-${dateStrList[1]}")
                selectedDate = LocalDate.parse(strDate)
                Log.d("TAG", "onViewCreated currentMonth: $currentMonth")
                setMonthCalendar(currentMonth)
            }
        }

        monthly_shimmer_frame_layout.startShimmerAnimation()

            // 어댑터
            monthlyMemoAdapter = MonthlyMemoAdapter(memoList, context!!, { memo ->
                val detailDialog = ScheduleDetailDialog(context!!)
                detailDialog.setOnModifyBtnClickedListener {
                    // 스케쥴 ID 보내기
                    val edit = ApplicationClass.sSharedPreferences.edit()
                    edit.putInt(Constants.EDIT_SCHEDULE_ID, memo.id)
                    edit.apply()
                    Constants.IS_EDIT = true

                    //바텀 시트 다이얼로그 확장
                    (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
                }
                detailDialog.start(memo, null)

            }, {
                // 일정삭제
                memo ->
                AskDialog(context!!)
                        .setTitle("일정삭제")
                        .setMessage("일정을 삭제하시겠습니까?")
                        .setPositiveButton("삭제") {
                            showLoadingDialog(context!!)
                            TodayService(this).onPutDeleteMemo(memo.id)
                        }
                        .setNegativeButton("취소") {
                        }.show()

            }, {
                // 공유
                val sendStringData = "${it.title}\n${it.description}\n${it.formDateStr}\n"
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, sendStringData)
                    type = "text/plain"
                }
                Log.d("monthly", "onViewCreated: share sendIntent ${sendIntent} ${sendIntent.resolveActivity((activity as MainActivity).packageManager) == null}")
                if (sendIntent.resolveActivity((activity as MainActivity).packageManager) != null) {
                    startActivity(sendIntent)
                }
            })
            binding.monthlyRecyclerview.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = monthlyMemoAdapter
            }
    }

    fun setUpCalendar(){
        class CalendarViewContainer(view: View): ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.calendar_day_text)
            lateinit var day:CalendarDay

            // 달력 일자 클릭 리스너
            init {
                Log.d("TAG", "setUpCalendar: calendar init")
                view.setOnClickListener {
                    if(day.owner == DayOwner.THIS_MONTH){
                        setPassivePreviousDayView()
                        val currentSelection = selectedDate
                        if(selectedDate == day.date){
                            if (currentSelection != null) {
                                binding.calendarView.notifyDateChanged(currentSelection)
                                selectedView = textView
                            }
                            MonthlyService(this@MonthlyFragment).onGetMonthlyMemoItems(selectedDate.toString())
                        }else{
                            selectedDate = day.date
                            MonthlyService(this@MonthlyFragment).onGetMonthlyMemoItems(selectedDate.toString())
                            textView.setBackgroundResource(R.drawable.background_item_calendar_today)
                            textView.elevation = 5f
                            selectedView = textView
                            binding.calendarView.notifyDateChanged(day.date)

                            if(currentSelection != null){
                                binding.calendarView.notifyDateChanged(currentSelection)
                            }
                        }
                    }
                }
            }
        }

        binding.calendarView.dayBinder = object:DayBinder<CalendarViewContainer>{
            override fun bind(container: CalendarViewContainer, day: CalendarDay) {

                container.textView.text = day.date.dayOfMonth.toString()
                // 해당 월의 날인 경우
                if(day.owner == DayOwner.THIS_MONTH){
                    container.textView.setTextColor(Color.BLACK)
                    userCheckedDateList.forEach {
                        // 메모가 있는 날짜들 백그라운드 변경
                        if(it == day.date){
                            if(todayDate != day.date){
                                container.textView.setBackgroundResource(R.drawable.background_item_calendar_has_memo)
                                container.textView.elevation = 0f
                            }
                        }
                    }
                    // 오늘 날짜
                    if(todayDate == day.date && selectedDate == null){
                        container.textView.setBackgroundResource(R.drawable.background_item_calendar_today)
                        container.textView.elevation = 5f
                        selectedView = container.textView
                    }
                }else{
                    // 해당 월의 날이 아닌 경우
                    container.textView.setTextColor(Color.GRAY)
                }
                container.day = day
            }
            override fun create(view: View): CalendarViewContainer = CalendarViewContainer(view)
        }


        binding.calendarView.setup(currentMonth.minusMonths(0),currentMonth.plusMonths(0),daysOfWeek.first())

        // 달력 헤더
        binding.calendarView.monthHeaderBinder = object:MonthHeaderFooterBinder<CalendarViewHeader>{
            override fun bind(container: CalendarViewHeader, month: CalendarMonth) {
                container.headerMonthTextTitle.text = "${CalendarConverter.monthToShortMonthName(month.yearMonth.month.name)}"
                container.headerLayoutYear.setOnTouchListener { _, event ->
                    when(event.action){
                        MotionEvent.ACTION_DOWN->{
                            val datePickBottomSheetDialog = DatePickBottomSheetDialog()
                            datePickBottomSheetDialog.show(
                                    childFragmentManager,
                                    datePickBottomSheetDialog.tag
                            )
                        }
                    }
                    false
                }
                container.headerBtnYearDatePicker.text = "${month.year}"
                container.headerBtnMonthPlus.setOnTouchListener { _, event ->
                    when(event.action){
                        MotionEvent.ACTION_DOWN->{
                            currentMonth = currentMonth.plusMonths(1)
                            setMonthCalendar(currentMonth)

                        }
                    }
                    false
                }
                container.headerBtnMonthMinus.setOnTouchListener { _, event ->
                    when(event.action){
                        MotionEvent.ACTION_DOWN->{
                            currentMonth = currentMonth.minusMonths(1)
                            setMonthCalendar(currentMonth)

                        }
                    }
                    false
                }
                container.headerBtnYearDatePicker.setOnClickListener {
                }
                // 현재요일 텍스트 색상 처리
                container.setDayStyleBold(LocalDate.now().dayOfWeek.toString())
            }


            override fun create(view: View): CalendarViewHeader  = CalendarViewHeader(view)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(!isVisibleToUser && isResumed){
            if(monthly_shimmer_frame_layout.isAnimationStarted){
                monthly_shimmer_frame_layout.stopShimmerAnimation()
            }
            if(selectedDate != null){
                selectedDate = null
            }
        }

    }

    fun setMonthCalendar(curMonth:YearMonth){
        val asyncDialog = LoadingDialog(context!!)
        val yearMonthString = curMonth.toString().split("-")
        targetYear = yearMonthString[0].toInt()
        if(yearMonthString[1].toInt() < 10){
            targetMonth = if(yearMonthString[1].contains("0")){
                yearMonthString[1].replace("0","").toInt()
            }else{
                yearMonthString[1].toInt()
            }
        }
        // job1 -> job2 -> dialog dismiss
        GlobalScope.launch(Dispatchers.Main) {
            val job1 = launch {
                MonthlyService(this@MonthlyFragment).onGetUserDateList(targetMonth!!,targetYear!!)
            }
            val job2 = launch{
                job1.join()
                binding.calendarView.setup(curMonth,curMonth,daysOfWeek.first())
                binding.calendarView.scrollToMonth(curMonth)
            }
            val asyncDialogJob = launch {
                asyncDialog.show()
                job2.join()
                delay(1000)
                asyncDialog.dismiss()
            }

        }


    }

    fun initializeMonthlyAdapter(newMemoList:ArrayList<MemoItem>){
        // 어댑터
        monthlyMemoAdapter = MonthlyMemoAdapter(newMemoList, context!!,{
            memo->
            val detailDialog = ScheduleDetailDialog(context!!)
            detailDialog.setOnModifyBtnClickedListener {
                // 스케쥴 ID 보내기
                val edit = ApplicationClass.sSharedPreferences.edit()
                edit.putInt(Constants.EDIT_SCHEDULE_ID, memo.id)
                edit.apply()
                Constants.IS_EDIT = true

                //바텀 시트 다이얼로그 확장
                (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
            }
            detailDialog.start(memo,memo.formDateStr)

        },{
            // 일정삭제
            memo->
            AskDialog(context!!)
                    .setTitle("일정삭제")
                    .setMessage("일정을 삭제하시겠습니까?")
                    .setPositiveButton("삭제"){
                        showLoadingDialog(context!!)
                        TodayService(this).onPutDeleteMemo(memo.id)
                    }
                    .setNegativeButton("취소"){
                    }.show()

        },{
            val sendStringData = "${it.title}\n${it.description}\n${it.formDateStr}\n"
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, sendStringData)
                type = "text/plain"
            }
            Log.d("monthly", "onViewCreated: share sendIntent ${sendIntent} ${sendIntent.resolveActivity((activity as MainActivity).packageManager) == null}")
            if (sendIntent.resolveActivity((activity as MainActivity).packageManager) != null) {
                startActivity(sendIntent)
            }

        })
        binding.monthlyRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = monthlyMemoAdapter
        }
    }

    override fun viewPagerApiRequest() {
        Log.d("TAG", "viewPagerApiRequest called ")
        super.viewPagerApiRequest()
        todayDate = LocalDate.now()
        targetYear = todayDate?.year
        targetMonth = todayDate?.monthValue

        GlobalScope.launch(Dispatchers.IO) {
            val job1 = launch {
                MonthlyService(this@MonthlyFragment).onGetUserDateList(targetMonth!!,targetYear!!)
            }
            val job2 = launch {
                MonthlyService(this@MonthlyFragment).onGetMonthlyMemoItems(todayDate.toString())
            }
            val job3 = launch(Dispatchers.Main) {
                delay(1000)
                try{
                    if(monthly_shimmer_frame_layout.isAnimationStarted){
                        monthly_shimmer_frame_layout.stopShimmerAnimation()
                        monthly_shimmer_frame_layout.visibility = View.GONE
                        binding.monthlyRecyclerview.visibility = View.VISIBLE
                        binding.calendarView.visibility = View.VISIBLE
                    }
                }catch (e:NullPointerException){
                    Log.d("monthlyFragment", "viewPagerApiRequest: shimmer null pointerException")
                }
            }

        }


    }

    fun setPassivePreviousDayView(){
        if(selectedView != null){
            selectedView!!.background = null
        }
    }


    override fun onGetMonthlyMemoItemSuccess(response: MonthlyMemoItemResponse) {
        if(response.isSuccess && response.code == 100){
            val responseJsonArray = response.data.asJsonArray
            memoList.clear()
            if(responseJsonArray.size() > 0) {
                responseJsonArray.forEach {
                    val memoJsonObject = it.asJsonObject
                    val scheduleId = memoJsonObject.get("scheduleID").asInt
                    val scheduleStringDate = memoJsonObject.get("scheduleDate").asString.split(" ")
                    val scheduleTitle = memoJsonObject.get("scheduleName").asString
                    val scheduleContentJsonObject:JsonElement? = memoJsonObject.get("scheduleMemo")
                    val scheduleCategoryIdJsonElement: JsonElement? = memoJsonObject.get("categoryID")
                    val scheduleColorInfoJsonElement: JsonElement? = memoJsonObject.get("colorInfo")
                    val scheduleDate = memoJsonObject.get("scheduleFormDate").asString
                    val memoScheduleFormDate = memoJsonObject.get("scheduleFormDate").asString
                    val memoScheduleOrder = memoJsonObject.get("scheduleOrder").asInt

                    val scheduleDayInt = scheduleStringDate[0].toInt()
                    val scheduleMonth = scheduleStringDate[1]

                    var scheduleContent = ""
                    if(!scheduleContentJsonObject!!.isJsonNull){
                        scheduleContent = scheduleContentJsonObject.asString
                    }
                    var scheduleCategoryColor:String? = null
                    if(!scheduleColorInfoJsonElement!!.isJsonNull){
                        scheduleCategoryColor = scheduleColorInfoJsonElement.asString
                    }
                    userMemos.add(scheduleDate)
                    memoList.add(MemoItem(scheduleId, scheduleMonth, scheduleDayInt, scheduleTitle, scheduleContent, false, scheduleCategoryColor,memoScheduleFormDate,memoScheduleOrder))
                }
            }
            initializeMonthlyAdapter(memoList)
        }else{
        }
    }

    override fun onGetMonthlyMemoItemFailure(message: String) {
    }

    override fun onGetAllMemosSuccess(response: AllMemoResponse) {
    }

    override fun onGetAllMemosFailure(message: String) {
    }

    override fun onGetUserDateListSuccess(response: MonthlyUserDateListResponse) {
        if(response.isSuccess && response.code == 100){
            val dateResultArray = response.result.asJsonArray
            dateResultArray.forEach {
                val isScheduleDay = it.asJsonObject.get("date").asString
                userCheckedDateList.add(LocalDate.parse(isScheduleDay))
            }
            Log.d("TAG", "onGetUserDateListSuccess: 유저 날짜 리스트 api success")
            setUpCalendar()

        }else{
            showCustomToast(response.message.toString())
        }
    }

    override fun onGetUserDateListFailure(message: String) {
    }

    override fun onGetScheduleItemsSuccess(response: ScheduleItemsResponse) {
    }

    override fun onGetScheduleItemsFailure(message: String) {
    }

    override fun onDeleteMemoSuccess(response: BaseResponse, scheduleID: Int) {
        if(response.isSuccess){
            when(response.code){
                100->{
                    showCustomToast(response.message.toString())
                    memoList.removeIf {
                        it.id == scheduleID
                    }
                    initializeMonthlyAdapter(memoList)
                    dismissLoadingDialog()
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
    }

    override fun onPostItemCheckFailure(message: String) {
    }

    override fun onGetUserTopCommentSuccess(response: TopCommentResponse) {
    }

    override fun onGetUserTopCommentFailure(message: String) {
    }

    override fun onPostSchedulePositionSuccess(response: BaseResponse) {
    }

    override fun onPostSchedulePositionFailure(message: String) {
    }

    override fun onGetDetailMemoSuccess(response: DetailMemoResponse) {
    }

    override fun onGetDetailMemoFailure(message: String) {
    }
}