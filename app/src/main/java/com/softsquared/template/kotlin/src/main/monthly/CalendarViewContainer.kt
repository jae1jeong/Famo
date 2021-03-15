package com.softsquared.template.kotlin.src.main.monthly

import android.util.Log
import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer
import com.softsquared.template.kotlin.R
import java.time.LocalDate

class CalendarViewContainer(view: View):ViewContainer(view) {
    private var selectedDate: LocalDate?= null
    val textView = view.findViewById<TextView>(R.id.calendar_day_text)
    lateinit var day:CalendarDay


}