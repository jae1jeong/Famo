package com.softsquared.template.kotlin.src.main.monthly

import android.graphics.Color
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

    // 요일
    val headerTextMon = view.findViewById<TextView>(R.id.calendar_header_text_mon)
    val headerTextTue = view.findViewById<TextView>(R.id.calendar_header_text_tue)
    val headerTextWed = view.findViewById<TextView>(R.id.calendar_header_text_wed)
    val headerTextThu = view.findViewById<TextView>(R.id.calendar_header_text_thu)
    val headerTextFri = view.findViewById<TextView>(R.id.calendar_header_text_fri)
    val headerTextSat = view.findViewById<TextView>(R.id.calendar_header_text_sat)
    val headerTextSun = view.findViewById<TextView>(R.id.calendar_header_text_sun)

    fun setDayStyleBold(dayName:String){
        when(dayName){
            "MONDAY"->{
                headerTextMon.setTextColor(Color.BLACK)
            }
            "TUESDAY"->{
                headerTextTue.setTextColor(Color.BLACK)
            }
            "WEDNESDAY"->{
                headerTextWed.setTextColor(Color.BLACK)
            }
            "THURSDAY"->{
                headerTextThu.setTextColor(Color.BLACK)
            }
            "FRIDAY"->{
                headerTextFri.setTextColor(Color.BLACK)
            }
            "SATURDAY"->{
                headerTextSat.setTextColor(Color.BLACK)
            }
            "SUNDAY"->{
                headerTextSun.setTextColor(Color.BLACK)
            }
            else->{
                "잘못된 요일 값"
            }
        }
    }


}