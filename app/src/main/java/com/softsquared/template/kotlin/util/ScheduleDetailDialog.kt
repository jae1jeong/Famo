package com.softsquared.template.kotlin.util

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.today.models.MemoItem

class ScheduleDetailDialog(context: Context) {
    private val dlg = Dialog(context)
    private lateinit var dialogContent:TextView
    private lateinit var dialogTitle:TextView
    private lateinit var modifyBtn: Button
    private lateinit var closeBtn: ImageView
    private lateinit var listener: scheduleDetailDialogClickListener

    interface scheduleDetailDialogClickListener{
        fun modifyBtnClicked()
    }

    fun start(memoItem: MemoItem){
        // 다이얼로그 제목 안보이게 하기
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dlg.setContentView(R.layout.dialog_detail_schedule)

        // 다이얼로그 제목
        dialogTitle = dlg.findViewById(R.id.dialog_detail_text_title)
        dialogTitle.text = memoItem.title

        // 다이얼로그 내용
        dialogContent = dlg.findViewById(R.id.dialog_detail_text_content)
        dialogContent.text = memoItem.description
        Log.d("dialog", "start: ${memoItem.description}")

        // 다이얼로그 수정하기 버튼 리스너
        modifyBtn = dlg.findViewById(R.id.dialog_detail_btn_modify)
        modifyBtn.setOnClickListener {
        listener.modifyBtnClicked()
            dlg.dismiss()
        }

        // 닫기 버튼 리스너
        closeBtn = dlg.findViewById(R.id.dialog_detail_btn_close)
        closeBtn.setOnClickListener {
            dlg.dismiss()
        }


        dlg.show()

    }

    fun setOnModifyBtnClickedListener(listener:()->Unit){
        this.listener = object:scheduleDetailDialogClickListener{
            override fun modifyBtnClicked() {
                listener()
            }

        }
    }
}