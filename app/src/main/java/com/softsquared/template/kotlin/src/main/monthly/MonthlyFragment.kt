package com.softsquared.template.kotlin.src.main.monthly

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonElement
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.config.BaseResponse
import com.softsquared.template.kotlin.databinding.FragmentMonthlyBinding
import com.softsquared.template.kotlin.src.main.models.DetailMemoResponse
import com.softsquared.template.kotlin.src.main.monthly.adapter.MonthlyMemoAdapter
import com.softsquared.template.kotlin.src.main.monthly.models.AllMemoResponse
import com.softsquared.template.kotlin.src.main.monthly.models.MonthlyMemoItemResponse
import com.softsquared.template.kotlin.src.main.monthly.models.MonthlyUserDateListResponse
import com.softsquared.template.kotlin.src.main.today.TodayFragment
import com.softsquared.template.kotlin.src.main.today.TodayService
import com.softsquared.template.kotlin.src.main.today.TodayView
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import com.softsquared.template.kotlin.src.main.today.models.ScheduleItemsResponse
import com.softsquared.template.kotlin.src.main.today.models.TopCommentResponse
import com.softsquared.template.kotlin.util.AskDialog
import com.softsquared.template.kotlin.util.CalendarConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList

class MonthlyFragment : BaseFragment<FragmentMonthlyBinding>(FragmentMonthlyBinding::bind, R.layout.fragment_monthly),MonthlyView,TodayView{
    lateinit var monthlyMemoAdapter:MonthlyMemoAdapter
    val memoList:ArrayList<MemoItem> = arrayListOf()
    private val userCheckedDateList:ArrayList<LocalDate> = arrayListOf()
    private var todayDate:LocalDate ?= null
    private var selectedDate:LocalDate ?= null
    private val userMemos:ArrayList<String> = arrayListOf()

    private var targetMonth:Int? = null
    private var targetYear:Int? = null

    private var currentMonth:YearMonth = YearMonth.now()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        class CalendarViewContainer(view: View): ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.calendar_day_text)
            lateinit var day:CalendarDay

            // 달력 일자 클릭 리스너
            init {
                Log.d("TAG", "onViewCreated: CalendarViewContainer init")
                selectedDate = LocalDate.now()
//                showCustomToast("$targetYear $targetMonth")
                view.setOnClickListener {
                    if(day.owner == DayOwner.THIS_MONTH){
                        val currentSelection = selectedDate
                        if(selectedDate == day.date){
                            if (currentSelection != null) {
                                binding.calendarView.notifyDateChanged(currentSelection)
                            }
                            showLoadingDialog(context!!)
                            MonthlyService(this@MonthlyFragment).onGetMonthlyMemoItems(selectedDate.toString())
                        }else{
                            selectedDate = day.date
                            showLoadingDialog(context!!)
                            MonthlyService(this@MonthlyFragment).onGetMonthlyMemoItems(selectedDate.toString())
                            textView.setBackgroundResource(R.drawable.background_item_calendar_today)
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
                if(day.owner == DayOwner.THIS_MONTH){
                    container.textView.setTextColor(Color.BLACK)
                    userCheckedDateList.forEach {
                        // 메모가 있는 날짜들 백그라운드 변경
                        if(it == day.date){
                            if(todayDate != day.date){
                                container.textView.setBackgroundResource(R.drawable.background_item_calendar_has_memo)
                            }
                        }
                    }
                    // 오늘 날짜
                    if(todayDate == day.date){
                        container.textView.setBackgroundResource(R.drawable.background_item_calendar_today)
                    }
                }else{
                    container.textView.setTextColor(Color.GRAY)
                }
                container.day = day
            }
            override fun create(view: View): CalendarViewContainer = CalendarViewContainer(view)
        }


        val firstMonth = currentMonth.minusMonths(0)
        val lastMonth = currentMonth.plusMonths(0)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        val daysOfWeek = arrayOf(
                DayOfWeek.SUNDAY,
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY
        )
        binding.calendarView.setup(firstMonth,lastMonth,daysOfWeek.first())


        // 달력 헤더
        binding.calendarView.monthHeaderBinder = object:MonthHeaderFooterBinder<CalendarViewHeader>{
                override fun bind(container: CalendarViewHeader, month: CalendarMonth) {
                    container.headerMonthTextTitle.text = "${CalendarConverter.monthToShortMonthName(month.yearMonth.month.name)}"
                    container.headerLayoutYear.setOnClickListener {
                        val datePickBottomSheetDialog = DatePickBottomSheetDialog()
                        datePickBottomSheetDialog.show(
                            childFragmentManager,
                            datePickBottomSheetDialog.tag
                        )
                    }
                    container.headerBtnYearDatePicker.text = "${month.year}"
                    container.headerBtnMonthPlus.setOnClickListener {
                        currentMonth = currentMonth.plusMonths(1)
                        month.yearMonth.plusMonths(1)
                        container.headerMonthTextTitle.text = "${month.yearMonth.month}"

                        binding.calendarView.setup(firstMonth,lastMonth,daysOfWeek.first())
                        Log.d("TAG", "bind: $currentMonth $month")
                        binding.calendarView.scrollToMonth(currentMonth)


                    }
                    container.headerBtnYearDatePicker.setOnClickListener {
                        month.yearMonth.minusMonths(1)
                        currentMonth = currentMonth.minusMonths(1)
                        binding.calendarView.setup(firstMonth,lastMonth,daysOfWeek.first())
                        Log.d("TAG", "bind: $currentMonth")
                        binding.calendarView.scrollToMonth(currentMonth)


                    }
                }


                override fun create(view: View): CalendarViewHeader  = CalendarViewHeader(view)

            }


            binding.calendarView.scrollToMonth(currentMonth)


            // 어댑터
            monthlyMemoAdapter = MonthlyMemoAdapter(memoList, context!!,{
                                                                        memo->


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

            })
            binding.monthlyRecyclerview.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = monthlyMemoAdapter
            }
        }


    override fun viewPagerApiRequest() {
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

                    val scheduleDayInt = scheduleStringDate[0].toInt()
                    val scheduleMonth = scheduleStringDate[1]

                    var scheduleContent = ""
                    if(!scheduleContentJsonObject!!.isJsonNull){
                        scheduleContent = scheduleContentJsonObject.asString
                    }
                    var scheduleCategoryColor:String? = null
                    if(!scheduleColorInfoJsonElement!!.isJsonNull){
                        scheduleCategoryColor = scheduleCategoryIdJsonElement.toString()
                    }
                    userMemos.add(scheduleDate)
                    memoList.add(MemoItem(scheduleId, scheduleMonth, scheduleDayInt, scheduleTitle, scheduleContent, false, scheduleCategoryColor))
                }
            }
            monthlyMemoAdapter.setNewMemoList(memoList)
        }else{
            showCustomToast(response.message.toString())
        }
    }

    override fun onGetMonthlyMemoItemFailure(message: String) {
        showCustomToast(message)
    }

    override fun onGetAllMemosSuccess(response: AllMemoResponse) {
        if(response.isSuccess && response.code == 100){
            val memoJsonArray = response.data.asJsonArray
            memoJsonArray.forEach {
                val memoJsonObject = it.asJsonObject
                val scheduleId = memoJsonObject.get("scheduleID").asInt
                val scheduleDate = memoJsonObject.get("scheduleDate").asString
                val scheduleTitle = memoJsonObject.get("scheduleName").asString
                val scheduleContent = memoJsonObject.get("scheduleMemo").asString
                val colorInfoJsonObject:JsonElement? = memoJsonObject.get("colorInfo")
            }

        }else{
            dismissLoadingDialog()
            showCustomToast(response.message.toString())
        }
    }

    override fun onGetAllMemosFailure(message: String) {
        dismissLoadingDialog()
        showCustomToast(message)
    }

    override fun onGetUserDateListSuccess(response: MonthlyUserDateListResponse) {
        if(response.isSuccess && response.code == 100){
            val dateResultArray = response.result.asJsonArray
            dateResultArray.forEach {
                val isScheduleDay = it.asJsonObject.get("date").asString
                userCheckedDateList.add(LocalDate.parse(isScheduleDay))
            }

        }else{
            showCustomToast(response.message.toString())
        }
    }

    override fun onGetUserDateListFailure(message: String) {
        showCustomToast(message)
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
                    monthlyMemoAdapter.setNewMemoList(memoList)
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