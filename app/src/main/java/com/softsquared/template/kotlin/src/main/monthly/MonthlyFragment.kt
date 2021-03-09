package com.softsquared.template.kotlin.src.main.monthly

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentMonthlyBinding
import com.softsquared.template.kotlin.src.main.monthly.adapter.MonthlyMemoAdapter
import com.softsquared.template.kotlin.src.main.today.models.MemoItem
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList

class MonthlyFragment : BaseFragment<FragmentMonthlyBinding>(FragmentMonthlyBinding::bind, R.layout.fragment_monthly){
    lateinit var monthlyMemoAdapter:MonthlyMemoAdapter
    val memoList:ArrayList<MemoItem> = arrayListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//         더미데이터로 리사이클러뷰 테스트
        for (i in 1..5) {
            memoList.add(MemoItem(i, "202021", i, "오늘", "내용1", false, "BLUE"))
            memoList.add(MemoItem(i, "202021", i, "테스트2", "내용2에요", true, "BLUE"))


            binding.calendarView.dayBinder = object:DayBinder<CalendarViewContainer>{
                override fun bind(container: CalendarViewContainer, day: CalendarDay) {
                    container.textView.text = day.date.dayOfMonth.toString()
                    if(day.owner == DayOwner.THIS_MONTH){
                        container.textView.setTextColor(Color.BLACK)
                    }else{
                        container.textView.setTextColor(Color.GRAY)
                    }
                }
                override fun create(view: View): CalendarViewContainer = CalendarViewContainer(view)
            }

//            binding.calendarView.monthHeaderBinder = object:MonthHeaderFooterBinder<Month>
            val currentMonth = YearMonth.now()
            val firstMonth = currentMonth.minusMonths(1)
            val lastMonth = currentMonth.plusMonths(1)
            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
            binding.calendarView.setup(firstMonth,lastMonth,firstDayOfWeek)
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