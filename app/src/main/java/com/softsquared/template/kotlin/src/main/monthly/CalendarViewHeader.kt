package com.softsquared.template.kotlin.src.main.monthly

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer
import com.softsquared.template.kotlin.R

class CalendarViewHeader(view: View):ViewContainer(view) {
    val headerMonthTextTitle = view.findViewById<TextView>(R.id.calendar_header_text_month_title)
    val headerBtnMonthPlus = view.findViewById<ImageView>(R.id.calendar_header_btn_right)
    val hedderBtnMonthMinus = view.findViewById<ImageView>(R.id.calendar_header_btn_left)

}