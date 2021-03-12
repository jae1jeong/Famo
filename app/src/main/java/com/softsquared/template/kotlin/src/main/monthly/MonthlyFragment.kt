package com.softsquared.template.kotlin.src.main.monthly

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentMonthlyBinding
import com.softsquared.template.kotlin.src.main.monthly.adapter.MonthlyMemoAdapter
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import kotlinx.coroutines.flow.callbackFlow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList

class MonthlyFragment : BaseFragment<FragmentMonthlyBinding>(FragmentMonthlyBinding::bind, R.layout.fragment_monthly){
    lateinit var monthlyMemoAdapter:MonthlyMemoAdapter
    val memoList:ArrayList<MemoItem> = arrayListOf()
    private val userCheckedDateList:ArrayList<LocalDate> = arrayListOf()
    private var todayDate:LocalDate ?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todayDate = LocalDate.now()

//         더미데이터로 리사이클러뷰 테스트
        for (i in 1..5) {
            memoList.add(MemoItem(i, "202021", i, "오늘", "내용1", false, "BLUE"))
            memoList.add(MemoItem(i, "202021", i, "테스트2", "내용2에요", true, "BLUE"))


            userCheckedDateList.add(LocalDate.parse("2021-03-31"))
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

            binding.calendarView.monthHeaderBinder = object:MonthHeaderFooterBinder<CalendarViewHeader>{
                override fun bind(container: CalendarViewHeader, month: CalendarMonth) {
                    container.headerMonthTextTitle.text = "${month.yearMonth.month}"
                }

                override fun create(view: View): CalendarViewHeader  = CalendarViewHeader(view)

            }
            val daysOfWeek = arrayOf(
                DayOfWeek.SUNDAY,
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY
            )
            val currentMonth = YearMonth.now()
            val firstMonth = currentMonth.minusMonths(0)
            val lastMonth = currentMonth.plusMonths(0)
            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
            binding.calendarView.setup(firstMonth,lastMonth,daysOfWeek.first())
            binding.calendarView.scrollToMonth(currentMonth)


            // 어댑터
            monthlyMemoAdapter = MonthlyMemoAdapter(memoList, context!!) {}
            binding.monthlyRecyclerview.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = monthlyMemoAdapter
            }

        }
    }
}