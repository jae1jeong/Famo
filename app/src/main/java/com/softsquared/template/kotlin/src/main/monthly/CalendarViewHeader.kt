package com.softsquared.template.kotlin.src.main.monthly

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer
import com.softsquared.template.kotlin.R

class CalendarViewHeader(view: View):ViewContainer(view) {
    val headerMonthTextTitle = view.findViewById<TextView>(R.id.calendar_header_text_month_title)
    val headerBtnMonthPlus = view.findViewById<ImageView>(R.id.calendar_header_btn_right)
    val headerBtnMonthMinus = view.findViewById<ImageView>(R.id.calendar_header_btn_left)
    val headerBtnYearDatePicker = view.findViewById<TextView>(R.id.calendar_header_text_year)
    val headerLayoutYear = view.findViewById<LinearLayout>(R.id.calendar_header_layout_year)

}