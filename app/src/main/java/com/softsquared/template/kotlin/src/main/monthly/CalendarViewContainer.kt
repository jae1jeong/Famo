package com.softsquared.template.kotlin.src.main.monthly

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer
import com.softsquared.template.kotlin.R

class CalendarViewContainer(view: View):ViewContainer(view) {

    val textView = view.findViewById<TextView>(R.id.calendar_day_text)
    val headerMonthTextTitle = view.findViewById<TextView>(R.id.calendar_header_text_month_title)
}