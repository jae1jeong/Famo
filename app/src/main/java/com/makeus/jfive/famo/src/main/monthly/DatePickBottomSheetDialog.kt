package com.makeus.jfive.famo.src.main.monthly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.makeus.jfive.famo.R
import com.makeus.jfive.famo.src.presentation.main.MainActivity
import kotlinx.android.synthetic.main.fragment_date_pick_bottom_sheet_dialog.*
import java.time.LocalDate

class DatePickBottomSheetDialog:BottomSheetDialogFragment() {

    // 날짜
    var strDate = ""
    // 날짜를 선택유무에 대한 변수
    var dateCnt = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_date_pick_bottom_sheet_dialog,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //날짜 변화에 대한
        val todayDate = LocalDate.now()
        monthly_datePicker_text_header.text= "${todayDate.year}년 ${todayDate.month.value}월"


        val listener = DatePicker.OnDateChangedListener { view, year, monthOfYear, dayOfMonth ->

            if (monthOfYear + 1 < 10 && dayOfMonth < 10) {
                strDate = year.toString() + "-" + "0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth
            } else if (monthOfYear + 1 >= 10 && dayOfMonth < 10) {
                strDate = year.toString() + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth
            } else if (monthOfYear + 1 < 10 && dayOfMonth >= 10) {
                strDate = year.toString() + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth
            } else {
                strDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
            }
            dateCnt++
            val bottomSheetDialogHeaderText= strDate.split("-")
            monthly_datePicker_text_header.text= "${bottomSheetDialogHeaderText[0]}년 ${bottomSheetDialogHeaderText[1].replace("0","")}월"
        }
        val month: Int = monthly_dataPicker.month
        val year = monthly_dataPicker.year
        val day = monthly_dataPicker.dayOfMonth
        monthly_dataPicker.init(year, month, day, listener)

        // 완료 버튼
        monthly_btn_finish.setOnClickListener {
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            val bundle = Bundle()
            bundle.putString("selectedDate",strDate)
            val monthlyFragment = MonthlyFragment()
            monthlyFragment.arguments = bundle
            transaction.add(R.id.main_view_pager,monthlyFragment)
            transaction.commit()
            dismiss()
        }
        // 취소 버튼
        monthly_btn_cancel.setOnClickListener {
            dismiss()
        }
    }



}