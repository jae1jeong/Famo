package com.makeus.jfive.famo.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.WindowManager
import com.makeus.jfive.famo.databinding.DialogDetailScheduleBinding
import com.makeus.jfive.famo.src.domain.model.main.DetailMemo
import com.makeus.jfive.famo.src.domain.model.month.MonthMemo
import com.makeus.jfive.famo.src.main.AddMemoService
import com.makeus.jfive.famo.src.presentation.main.MainActivity
import java.time.LocalDate

class ScheduleDetailDialog(val context: Context) {
    private val dlg = Dialog(context)
    private lateinit var listener: scheduleDetailDialogClickListener
    private var categoryName: String? = null
    private lateinit var _binding: DialogDetailScheduleBinding
    private val binding get() = _binding

    interface scheduleDetailDialogClickListener {
        fun modifyBtnClick()
    }


    fun start(memoItem: MonthMemo, topTitle: String?) {
        setUp()
        dlg.show()
    }

    private fun setUp() {
        // 다이얼로그 제목 안보이게 하기
        _binding = DialogDetailScheduleBinding.inflate(dlg.layoutInflater)
        dlg.setContentView(binding.root)
        val params = dlg.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dlg.window?.attributes = params
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 다이얼로그 수정하기 버튼 리스너
        binding.dialogDetailBtnModify.setOnClickListener {
            listener.modifyBtnClick()
            dlg.dismiss()
        }

        // 닫기 버튼 리스너
        binding.dialogDetailBtnClose.setOnClickListener {
            dlg.dismiss()
        }
    }

    fun startEdited(detailMemo: DetailMemo) {
        setUp()
        setContent(detailMemo)
        dlg.show()
    }

    fun setOnModifyBtnClickedListener(mListener:scheduleDetailDialogClickListener) {
        this.listener = mListener
    }


    private fun setContent(monthMemo: DetailMemo) {
        // 다이얼로그 제목
        binding.dialogDetailTextTitle.text = monthMemo.scheduleName
        // 다이얼로그 내용
        binding.dialogDetailTextContent.text = monthMemo.scheduleMemo
        // 다이얼로그 상단
        binding.dialogDetailTextDate.text = "${monthMemo.scheduleForm} ${monthMemo.scheduleDate}"
        binding.dialogDetailTextCategory.text = monthMemo.categoryName
        val shape = GradientDrawable()
        shape.cornerRadius = 180f
        shape.setColorFilter(Color.parseColor(CategoryColorPicker.setCategoryColor(monthMemo.colorInfo)), PorterDuff.Mode.SRC_IN)
        binding.dialogDetailTextCategory.setTextColor(Color.WHITE)
        binding.dialogDetailTextCategory.background = shape
    }

}