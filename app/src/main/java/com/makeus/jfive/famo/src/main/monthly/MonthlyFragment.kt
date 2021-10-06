package com.makeus.jfive.famo.src.main.monthly

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonElement
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.config.ApplicationClass
import com.makeus.jfive.famo.config.BaseFragment
import com.makeus.jfive.famo.config.BaseResponse
import com.makeus.jfive.famo.databinding.FragmentMonthlyBinding
import com.makeus.jfive.famo.src.common.Resource
import com.makeus.jfive.famo.src.domain.model.month.MonthMemo
import com.makeus.jfive.famo.src.presentation.main.MainActivity
import com.makeus.jfive.famo.src.main.models.DetailMemoResponse
import com.makeus.jfive.famo.src.main.monthly.adapter.MonthlyMemoAdapter
import com.makeus.jfive.famo.src.main.monthly.models.AllMemoResponse
import com.makeus.jfive.famo.src.main.monthly.models.MonthlyMemoItemResponse
import com.makeus.jfive.famo.src.main.monthly.models.MonthlyUserDateListResponse
import com.makeus.jfive.famo.src.main.today.TodayService
import com.makeus.jfive.famo.src.main.today.TodayView
import com.makeus.jfive.famo.src.main.today.models.ScheduleItemsResponse
import com.makeus.jfive.famo.src.main.today.models.TopCommentResponse
import com.makeus.jfive.famo.src.presentation.monthly.MonthlyFragmentViewModel
import com.makeus.jfive.famo.util.*
import dagger.hilt.android.AndroidEntryPoint
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
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class MonthlyFragment :
    BaseFragment<FragmentMonthlyBinding>(FragmentMonthlyBinding::bind, R.layout.fragment_monthly),
    MonthlyView, TodayView {
    val memoList: ArrayList<MonthMemo> = arrayListOf()
    private val userCheckedDateList: ArrayList<LocalDate> = arrayListOf()
    private var todayDate: LocalDate? = null
    private var selectedDate: LocalDate? = null
    private val userMemos: ArrayList<String> = arrayListOf()

    @Inject
    lateinit var mSharedPreferences: SharedPreferences
    private var selectedView: TextView? = null
    private var targetMonth: Int? = null
    private var targetYear: Int? = null

    val daysOfWeek = arrayOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )

    private val monthlyFragmentViewModel: MonthlyFragmentViewModel by viewModels()

    companion object {
        var monthlyMemoAdapter: MonthlyMemoAdapter? = null
    }


    private var currentMonth: YearMonth = YearMonth.now()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            val strDate = bundle.getString("selectedDate")
            if (strDate != "") {
                Log.d("TAG", "onViewCreated: $strDate")
                val dateStrList = strDate?.split("-")
                currentMonth = YearMonth.parse("${dateStrList?.get(0)!!}-${dateStrList[1]}")
                selectedDate = LocalDate.parse(strDate)
                Log.d("TAG", "onViewCreated currentMonth: $currentMonth")
                setMonthCalendar(currentMonth)
            }
        }
        monthly_shimmer_frame_layout.startShimmerAnimation()
        initViewModel()
        initAdapter()
        todayDate = LocalDate.now()
        targetYear = todayDate?.year
        targetMonth = todayDate?.monthValue

        GlobalScope.launch(Dispatchers.IO) {
            val job1 = launch {
                MonthlyService(this@MonthlyFragment).onGetUserDateList(targetMonth!!, targetYear!!)
            }
            val job2 = launch {
                MonthlyService(this@MonthlyFragment).onGetMonthlyMemoItems(todayDate.toString())
            }
            val job3 = launch(Dispatchers.Main) {
                delay(1000)
                try {
                    if (monthly_shimmer_frame_layout.isAnimationStarted) {
                        monthly_shimmer_frame_layout.stopShimmerAnimation()
                        monthly_shimmer_frame_layout.visibility = View.GONE
                        binding.monthlyRecyclerview.visibility = View.VISIBLE
                        binding.calendarView.visibility = View.VISIBLE
                    }
                } catch (e: NullPointerException) {
                    Log.d("monthlyFragment", "viewPagerApiRequest: shimmer null pointerException")
                }
            }

        }
    }

    private fun initAdapter(){
        // 어댑터
        monthlyMemoAdapter = MonthlyMemoAdapter(
            memoList,
            requireContext(),
            dialogModifyBtnClick,
            dialogDeleteBtnClick,
            dialogShareBtnClick
        )
        binding.monthlyRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = monthlyMemoAdapter
        }
    }

    private val dialogShareBtnClick: (MonthMemo) -> Unit = {
        // 공유
        val sendStringData = "${it.scheduleName}\n${it.scheduleMemo}\n${it.scheduleFormDate}\n"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, sendStringData)
            type = "text/plain"
        }

        if (sendIntent.resolveActivity((activity as MainActivity).packageManager) != null) {
            startActivity(sendIntent)
        }
    }

    private val dialogDeleteBtnClick: (MonthMemo) -> Unit = {
        // 일정삭제
            memo ->
        AskDialog(requireContext())
            .setTitle("일정삭제")
            .setMessage("일정을 삭제하시겠습니까?")
            .setPositiveButton("삭제") {
                showLoadingDialog(requireContext())
                TodayService(this).onPutDeleteMemo(memo.scheduleID)
            }
            .setNegativeButton("취소") {
            }.show()
    }

    private val dialogModifyBtnClick: (MonthMemo) -> Unit = {
        val detailDialog = ScheduleDetailDialog(requireContext())
//        detailDialog.setOnModifyBtnClickedListener {
//            // 스케쥴 ID 보내기
//            val edit = mSharedPreferences.edit()
//            edit.putInt(Constants.EDIT_SCHEDULE_ID, it.scheduleID)
//            edit.apply()
//            Constants.IS_EDIT = true
//
//            //바텀 시트 다이얼로그 확장
//            (activity as MainActivity).stateChangeBottomSheet(Constants.EXPAND)
//        }
//        detailDialog.start(it, null)
    }

    private fun initViewModel() {
        monthlyFragmentViewModel.monthlyListResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {

                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        })
        monthlyFragmentViewModel.monthByDateResponse.observe(viewLifecycleOwner,
            { memoByDateResponse ->
                when (memoByDateResponse) {
                    is Resource.Success -> {
                        memoList.clear()
                        monthlyMemoAdapter?.setNewMemoList(memoByDateResponse.data?.data as ArrayList<MonthMemo>)
                    }
                    is Resource.Error -> {
                        Log.e("TAG", "initViewModel: ${memoByDateResponse.throwable}")
                    }
                }
            })
    }

    fun setUpCalendar() {
        class CalendarViewContainer(view: View) : ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.calendar_day_text)
            lateinit var day: CalendarDay

            // 달력 일자 클릭 리스너
            init {
                Log.d("TAG", "setUpCalendar: calendar init")
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        setPassivePreviousDayView()
                        val currentSelection = selectedDate
                        if (selectedDate == day.date) {
                            if (currentSelection != null) {
                                binding.calendarView.notifyDateChanged(currentSelection)
                                selectedView = textView
                            }
                            monthlyFragmentViewModel.getMonthByDate(selectedDate.toString())
                        } else {
                            selectedDate = day.date
                            monthlyFragmentViewModel.getMonthByDate(selectedDate.toString())
                            textView.setBackgroundResource(R.drawable.background_item_calendar_today)
                            textView.elevation = 5f
                            selectedView = textView
                            binding.calendarView.notifyDateChanged(day.date)

                            if (currentSelection != null) {
                                binding.calendarView.notifyDateChanged(currentSelection)
                            }
                        }
                    }
                }
            }
        }

        binding.calendarView.dayBinder = object : DayBinder<CalendarViewContainer> {
            override fun bind(container: CalendarViewContainer, day: CalendarDay) {

                container.textView.text = day.date.dayOfMonth.toString()
                // 해당 월의 날인 경우
                if (day.owner == DayOwner.THIS_MONTH) {
                    container.textView.setTextColor(Color.BLACK)
                    userCheckedDateList.forEach {
                        // 메모가 있는 날짜들 백그라운드 변경
                        if (it == day.date) {
                            if (todayDate != day.date) {
                                container.textView.setBackgroundResource(R.drawable.background_item_calendar_has_memo)
                                container.textView.elevation = 0f
                            }
                        }
                    }
                    // 오늘 날짜
                    if (todayDate == day.date && selectedDate == null) {
                        container.textView.setBackgroundResource(R.drawable.background_item_calendar_today)
                        container.textView.elevation = 5f
                        selectedView = container.textView
                    }
                } else {
                    // 해당 월의 날이 아닌 경우
                    container.textView.setTextColor(Color.GRAY)
                }
                container.day = day
            }

            override fun create(view: View): CalendarViewContainer = CalendarViewContainer(view)
        }


        binding.calendarView.setup(
            currentMonth.minusMonths(0),
            currentMonth.plusMonths(0),
            daysOfWeek.first()
        )

        // 달력 헤더
        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<CalendarViewHeader> {
                override fun bind(container: CalendarViewHeader, month: CalendarMonth) {
                    container.headerMonthTextTitle.text =
                        "${CalendarConverter.monthToShortMonthName(month.yearMonth.month.name)}"
                    container.headerLayoutYear.setOnTouchListener { _, event ->
//                    when(event.action){
//                        MotionEvent.ACTION_DOWN->{
//                            val datePickBottomSheetDialog = DatePickBottomSheetDialog()
//                            datePickBottomSheetDialog.show(
//                                    childFragmentManager,
//                                    datePickBottomSheetDialog.tag
//                            )
//                        }
//                    }
                        false
                    }
                    container.headerBtnYearDatePicker.text = "${month.year}"
                    container.headerBtnMonthPlus.setOnTouchListener { _, event ->
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                currentMonth = currentMonth.plusMonths(1)
                                setMonthCalendar(currentMonth)

                            }
                        }
                        false
                    }
                    container.headerBtnMonthMinus.setOnTouchListener { _, event ->
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
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


                override fun create(view: View): CalendarViewHeader = CalendarViewHeader(view)
            }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isVisibleToUser && isResumed) {
            if (monthly_shimmer_frame_layout.isAnimationStarted) {
                monthly_shimmer_frame_layout.stopShimmerAnimation()
            }
            if (selectedDate != null) {
                selectedDate = null
            }
        }

    }

    fun setMonthCalendar(curMonth: YearMonth) {
        val asyncDialog = LoadingDialog(requireContext())
        val yearMonthString = curMonth.toString().split("-")
        targetYear = yearMonthString[0].toInt()
        if (yearMonthString[1].toInt() < 10) {
            targetMonth = if (yearMonthString[1].contains("0")) {
                yearMonthString[1].replace("0", "").toInt()
            } else {
                yearMonthString[1].toInt()
            }
        }
        // job1 -> job2 -> dialog dismiss
        GlobalScope.launch(Dispatchers.Main) {
            val job1 = launch {
                MonthlyService(this@MonthlyFragment).onGetUserDateList(targetMonth!!, targetYear!!)
            }
            val job2 = launch {
                job1.join()
                binding.calendarView.setup(curMonth, curMonth, daysOfWeek.first())
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

    fun initializeMonthlyAdapter(newMemoList: ArrayList<MonthMemo>) {
        // 어댑터
        monthlyMemoAdapter = MonthlyMemoAdapter(newMemoList, requireContext(), { memo ->

        }, {
            // 일정삭제
                memo ->
            AskDialog(requireContext())
                .setTitle("일정삭제")
                .setMessage("일정을 삭제하시겠습니까?")
                .setPositiveButton("삭제") {
                    showLoadingDialog(requireContext())
                    TodayService(this).onPutDeleteMemo(memo.scheduleID)
                }
                .setNegativeButton("취소") {
                }.show()

        }, {
            val sendStringData = "${it.scheduleName}\n${it.scheduleMemo}\n${it.scheduleFormDate}\n"
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, sendStringData)
                type = "text/plain"
            }
            Log.d(
                "monthly",
                "onViewCreated: share sendIntent ${sendIntent} ${sendIntent.resolveActivity((activity as MainActivity).packageManager) == null}"
            )
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

    }

    fun setPassivePreviousDayView() {
        if (selectedView != null) {
            selectedView!!.background = null
        }
    }


    override fun onGetMonthlyMemoItemSuccess(response: MonthlyMemoItemResponse) {
        if (response.isSuccess && response.code == 100) {
            val responseJsonArray = response.data.asJsonArray
            if (responseJsonArray.size() > 0) {
                responseJsonArray.forEach {
                    val memoJsonObject = it.asJsonObject
                    val scheduleId = memoJsonObject.get("scheduleID").asInt
                    val scheduleStringDate = memoJsonObject.get("scheduleDate").asString.split(" ")
                    val scheduleTitle = memoJsonObject.get("scheduleName").asString
                    val scheduleContentJsonObject: JsonElement? = memoJsonObject.get("scheduleMemo")
                    val scheduleCategoryIdJsonElement: JsonElement? =
                        memoJsonObject.get("categoryID")
                    val scheduleColorInfoJsonElement: JsonElement? = memoJsonObject.get("colorInfo")
                    val scheduleDate = memoJsonObject.get("scheduleFormDate").asString
                    val memoScheduleFormDate = memoJsonObject.get("scheduleFormDate").asString
                    val memoScheduleOrder = memoJsonObject.get("scheduleOrder").asInt

                    val scheduleDayInt = scheduleStringDate[0].toInt()
                    val scheduleMonth = scheduleStringDate[1]

                    var scheduleContent = ""
                    if (!scheduleContentJsonObject!!.isJsonNull) {
                        scheduleContent = scheduleContentJsonObject.asString
                    }
                    var scheduleCategoryColor: String? = null
                    if (!scheduleColorInfoJsonElement!!.isJsonNull) {
                        scheduleCategoryColor = scheduleColorInfoJsonElement.asString
                    }
//                    memoList.clear()
//                    userMemos.add(scheduleDate)
//                    memoList.add(MemoItem(scheduleId, scheduleMonth, scheduleDayInt, scheduleTitle, scheduleContent, false, scheduleCategoryColor,memoScheduleFormDate,memoScheduleOrder))
                }
            }
            initializeMonthlyAdapter(memoList)
        } else {
        }
    }

    override fun onGetMonthlyMemoItemFailure(message: String) {
    }

    override fun onGetAllMemosSuccess(response: AllMemoResponse) {
    }

    override fun onGetAllMemosFailure(message: String) {
    }

    override fun onGetUserDateListSuccess(response: MonthlyUserDateListResponse) {
        if (response.isSuccess && response.code == 100) {
            val dateResultArray = response.result.asJsonArray
            dateResultArray.forEach {
                val isScheduleDay = it.asJsonObject.get("date").asString
                userCheckedDateList.add(LocalDate.parse(isScheduleDay))
            }
            Log.d("TAG", "onGetUserDateListSuccess: 유저 날짜 리스트 api success")
            setUpCalendar()

        } else {
        }
    }

    override fun onGetUserDateListFailure(message: String) {
    }

    override fun onGetScheduleItemsSuccess(response: ScheduleItemsResponse) {
    }

    override fun onGetScheduleItemsFailure(message: String) {
    }

    override fun onDeleteMemoSuccess(response: BaseResponse, scheduleID: Int) {
        if (response.isSuccess) {
            when (response.code) {
                100 -> {
                    memoList.removeIf {
                        it.scheduleID == scheduleID
                    }
                    initializeMonthlyAdapter(memoList)
                    dismissLoadingDialog()
                }
                else -> {
                    dismissLoadingDialog()
                }
            }
        } else {
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