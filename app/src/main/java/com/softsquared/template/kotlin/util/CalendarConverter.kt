package com.softsquared.template.kotlin.src.util

object CalendarConverter {
    fun dayToKoreanShortDayName(day:String):String{
        return when(day){
            "MONDAY"-> "월"
            "TUESDAY"-> "화"
            "WEDNESDAY"-> "수"
            "THURSDAY"-> "목"
            "FRIDAY"-> "금"
            "SATURDAY"->"토"
            "SUNDAY"->"일"
            else-> "알 수 없음"
        }
    }

    fun monthToShortMonthName(month:String):String{
        return when(month){
            "JANUARY"->"Jan"
            "FEBRUARY"->"Feb"
            "MARCH"->"Mar"
            "APRIL"->"Apr"
            "MAY"->"May"
            "JUNE"->"Jun"
            "JULY"->"Jul"
            "AUGUST"->"Aug"
            "SEPTEMBER"->"Sept"
            "OCTOBER"->"Oct"
            "NOVEMBER"->"Nov"
            "DECEMBER"->"Dec"
            else->"알 수 없음"
        }
    }
}